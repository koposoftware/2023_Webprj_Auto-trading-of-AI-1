import json
import pandas as pd
import re

from flask import Flask  # 서버 구현을 위한 Flask 객체 import
from flask_restx import Api, Resource  # Api 구현을 위한 Api 객체 import
from pykrx import stock
from PredictModel import PredictModel
from KospiData import KospiData

app = Flask(__name__)  # Flask 객체 선언, 파라미터로 어플리케이션 패키지의 이름을 넣어줌.
api = Api(app)  # Flask 객체에 Api 객체 등록


# ---------------------------------------------------------------------------------------------------------------------#
# Restful API : 데코레이터 이용. 해당 경로에 클래스 등록
@api.route("/stock_info/ohlcv/<string:date>")
class OhlcvInfo(Resource):
    def get(self, date):  # GET 요청시 리턴 값에 해당 하는 dict를 JSON 형태로 반환
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
                columns={"티커": "ISIN", "시가": "OPEN", "고가": "HIGH", "저가": "LOW", "종가": "CLOSE", "거래량": "VOLUME", "거래대금": "AMOUNT", "등락률": "UPDOWN"})
            print(new_ohlcv)

            # 저장한 DF를 Json 파일로 변환
            new_ohlcv = new_ohlcv.to_json(orient="records")

            result = json.dumps(new_ohlcv, ensure_ascii=False)
            result = result.replace('\\', '')

            return result

        else:
            return pd.DataFrame([]).to_json(orient="records")

@api.route("/stock_info/fundamental/<string:date>")
class FundamentalInfo(Resource):
    def get(self, date):  # GET 요청시 리턴 값에 해당 하는 dict를 JSON 형태로 반환
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

@api.route("/stock_info/predict/<string:date>")
class Predict(Resource):
    def get(self, date):  # GET 요청시 리턴 값에 해당 하는 dict를 JSON 형태로 반환
        try:
            return PredictModel.predictToday(date).to_json(orient="records")
        except:
            return pd.DataFrame([]).to_json(orient="records")
        
@api.route("/stock_info/score/<string:date>")
class Score(Resource):
    def get(self, date):  # GET 요청시 리턴 값에 해당 하는 dict를 JSON 형태로 반환
        try:
            return PredictModel.scoreYesterday(date).to_json(orient="records")
        except:
            return pd.DataFrame([]).to_json(orient="records")
        
@api.route("/stock_info/kospi/<string:date>")
class Kospi(Resource):
    def get(self, date):  # GET 요청시 리턴 값에 해당 하는 dict를 JSON 형태로 반환
        try:
            return KospiData.getKospiData(date).to_json(orient="records")
        except:
            return pd.DataFrame([{"망":"함"}]).to_json(orient="records")
        
if __name__ == "__main__":
    app.debug = True
    app.run(debug=True, host='0.0.0.0', port=2473)
