import pandas as pd
from pykrx import stock as st
import pickle

close_model = pickle.load(open('PredictModel/predict_close_model_new.sav', 'rb'))
updown_model = pickle.load(open('PredictModel/predict_updown_model_new.sav', 'rb'))
date_label = pickle.load(open('PredictModel/date_label.sav', 'rb'))
stock_label = pickle.load(open('PredictModel/stock_label.sav', 'rb'))

# 1. 날짜 라벨인코더 업데이트 -> pickle로 저장하기 clear
def updateLabelEncoder(today):
    global date_label
    maxLabel = max(date_label['num'])
    todayDate = st.get_market_ohlcv(today, today, "005930").reset_index()

    for s_date in todayDate['날짜'].astype(str):
        if s_date not in date_label['s_date'].values:
            new_row = pd.DataFrame({'s_date': [s_date], 'num': [maxLabel + 1]})
            date_label = pd.concat([date_label, new_row], ignore_index=True)
    pickle.dump(date_label, open('PredictModel/date_label.sav', 'wb'))

# 2. 가져온 전일, 당일 시세 정보 전처리 : 가격예측&등락예측 모델용
def preprocessingNewData(today):
    updateLabelEncoder(today)
    columnMapper = {"티커":"isin", "시가" : "open", "고가" : "high", "저가" : "low", "종가" : "close", "거래량" : "volume", "거래대금" : "amount", "등락률" : "updown"}

    result = pd.DataFrame([])
    date_set = date_label.tail(2)['s_date'].str.replace("-",'').to_list()
    date_set_dash = date_label.tail(2)['s_date'].to_list()

    for i, date in enumerate(date_set):
        new = st.get_market_ohlcv(date).reset_index()
        new["s_date"] = date_set_dash[i]
        result = pd.concat([result,new])

    result.rename(columnMapper, axis=1, inplace=True)
    result = result[['isin', 's_date', 'open', 'high', 'low', 'close', 'volume', 'amount', 'updown']].sort_values(['isin', 's_date'])
    result['r_price'] = result['close'].shift(-1)
    result['r_updown'] = (0.5 * (result['updown'].shift(-1) / abs(result['updown'].shift(-1))) + 0.5)
    result.fillna(0, inplace= True)
    result['isin'] = result['isin'].replace(stock_label.set_index('isin')['num'])
    score = result[result["s_date"] != date_set_dash[-1]].reset_index(drop=True)
    predict = result[result["s_date"] == date_set_dash[-1]].reset_index(drop=True)
    score['s_date'] = score['s_date'].replace(date_label.set_index('s_date')['num'])
    predict['s_date'] = predict['s_date'].replace(date_label.set_index('s_date')['num'])
    r_price = score.pop("r_price")
    r_updown = score.pop("r_updown")
    predict.pop("r_price")
    predict.pop("r_updown")

    return [score, r_price, r_updown, predict]

# 3. 예측 결과 테이블 작성 및 json 반환
def predictToday(today):
    [_, r_price, r_updown, new_data] = preprocessingNewData(today)
    p_price = pd.DataFrame(close_model.predict(new_data), columns=['p_price'])
    e_updown = pd.DataFrame(updown_model.predict(new_data), columns=['e_updown'])

    result = pd.concat([new_data, p_price, e_updown], axis=1)
    result['p_price'] = round(result['p_price']).astype(int)
    result['e_updown'] = (round(result['e_updown'], 1) + 0.5).astype(int)
    result['p_rate'] = round((result['p_price'] - result['close']) / result['close'] * 100, 2)
    result['tmp_updown'] = (result['p_rate'] > 0).astype(int)
    result['predict'] = ((result['e_updown'] == 1) & (result['tmp_updown'] == 1)).astype(int)
    result.drop(['open', 'high', 'low', 'volume', 'amount', 'updown', "tmp_updown", 'e_updown'], axis =1, inplace=True)
    result['isin'].replace(stock_label.set_index('num')['isin'], inplace=True)
    result['s_date'].replace(date_label.set_index('num')['s_date'], inplace=True)
    return result

# 4. 예측 평가 테이블 작성 및 json 반환
def scoreYesterday(today):
    [new_data, r_price, r_updown, _] = preprocessingNewData(today)
    p_price = pd.DataFrame(close_model.predict(new_data), columns=['p_price'])
    e_updown = pd.DataFrame(updown_model.predict(new_data), columns=['e_updown'])

    result = pd.concat([new_data, p_price, r_price , e_updown, r_updown], axis=1)
    result['p_price'] = round(result['p_price']).astype(int)
    result['e_updown'] = (round(result['e_updown'], 1) + 0.5).astype(int)
    result['p_rate'] = round((result['p_price'] - result['close']) / result['close'] * 100, 2)
    result['r_rate'] = round((result['r_price'] - result['close']) / result['close'] * 100, 2)
    result['tmp_updown'] = (result['p_rate'] > 0).astype(int)
    result['predict'] = ((result['e_updown'] == 1) & (result['tmp_updown'] == 1)).astype(int)
    result['correct'] = (result['predict']==result['r_updown']).astype(int)
    result['error'] = round((abs((result['p_price'] - result['r_price']) / result['r_price']) * 100), 2)
    result.drop(['open', 'high', 'low', 'volume', 'amount', 'updown', 'tmp_updown', "e_updown","r_updown"], axis =1, inplace=True)
    result['isin'].replace(stock_label.set_index('num')['isin'], inplace=True)
    result['s_date'].replace(date_label.set_index('num')['s_date'], inplace=True)
    return result