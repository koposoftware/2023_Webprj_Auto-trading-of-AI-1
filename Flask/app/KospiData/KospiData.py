import pandas as pd
from pykrx import stock
import re
# import pickle

# kospi = pickle.load(open('KospiData/kospi.sav', 'rb'))

def getKospiData(date):
    kospi = stock.get_index_ohlcv("20130101",date,"1001").reset_index().astype(str)
    kospi['연도'] = kospi['날짜'].str[:4]
    kospi['월'] = kospi['날짜'].str[5:7]
    kospi['일'] = kospi['날짜'].str[8:10]
    kospi.rename({'연도' : 'year', '월' : 'month', '일' : 'day', '종가' : 'value'}, axis=1, inplace=True)
    kospi['time'] = kospi[['year', 'month', 'day']].apply(lambda x: {'year': x['year'], 'month': x['month'], 'day': x['day']}, axis=1)
    kospi = kospi[['time','value']]
    return kospi
