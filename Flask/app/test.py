import json
import pandas as pd
import re
from pykrx import stock


def get(date):  # GET 요청시 리턴 값에 해당 하는 dict를 JSON 형태로 반환
    pattern = r'^\d{4}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])$'
    if (len(date) == 8 and re.match(pattern, date)):
        # 당일 주가 정보와 재무지표를 받아와 DF로 저장
        new_ohlcv = stock.get_market_ohlcv(date)
        new_ohlcv = new_ohlcv.reset_index()
        print("가져옴ㅎ")

        # 날짜 col 생성
        new_ohlcv["S_DATE"] = date
        print("날짜")

        # col 이름 변경
        new_ohlcv = new_ohlcv.rename(
            columns={"티커": "ISIN", "시가": "OPEN", "고가": "HIGH", "저가": "LOW", "종가": "CLOSE", "거래량": "VOLUME",
                     "거래대금": "AMOUNT", "등락률": "UPDOWN"})
        print(new_ohlcv)

        # 저장한 DF를 Json 파일로 변환
        new_ohlcv = new_ohlcv.to_json(orient="records")

        result = json.dumps(new_ohlcv, ensure_ascii=False)
        result = result.replace('\\', '')

        return result

    else:
        return pd.DataFrame([]).to_json(orient="records")


def get2(date):  # GET 요청시 리턴 값에 해당 하는 dict를 JSON 형태로 반환
    pattern = r'^\d{4}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])$'
    if (len(date) == 8 and re.match(pattern, date)):
        # 당일 주가 정보와 재무지표를 받아와 DF로 저장
        new_fundamental = stock.get_market_fundamental(date)
        new_fundamental = new_fundamental.reset_index()
        print("가져옴ㅎ")

        # 날짜 col 생성
        new_fundamental["S_DATE"] = date
        print("날짜")

        # col 이름 변경
        new_fundamental = new_fundamental.rename(columns={"티커": "ISIN"})
        print(new_fundamental)

        # 저장한 DF를 Json 파일로 변환
        new_fundamental = new_fundamental.to_json(orient="records")

        result = json.dumps(new_fundamental, ensure_ascii=False)
        result = result.replace('\\', '')

        return result

    else:
        return pd.DataFrame([]).to_json(orient="records")


result = get("20230705")
result2 = get2("20230705")

with open('ohlcv.json', 'w', encoding='utf-8') as new_file:
    new_file.write(result)

with open('fundamental.json', 'w', encoding='utf-8') as new_file:
    new_file.write(result)
