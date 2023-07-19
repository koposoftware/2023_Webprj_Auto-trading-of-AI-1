select ohlcv.isin, ohlcv.s_date, 
adjusted.open, adjusted.high, adjusted.low, adjusted.close, 
ohlcv.volume, ohlcv.amount, ohlcv.updown
from
ohlcv, adjusted
where ohlcv.isin = adjusted.isin 
and ohlcv.s_date = adjusted.s_date ;

UPDATE ohlcv o
SET (o.open, o.high, o.low, o.close) =
    (SELECT a.open, a.high, a.low, a.close
     FROM adjusted a
     WHERE o.isin = a.isin AND o.s_date = a.s_date);

MERGE INTO ohlcv o
USING (
    SELECT a.isin, a.s_date, a.open AS new_open, a.high AS new_high, a.low AS new_low, a.close AS new_close
    FROM adjusted a
) a
ON (o.isin = a.isin AND o.s_date = a.s_date)
WHEN MATCHED THEN
    UPDATE SET o.open = a.new_open, o.high = a.new_high, o.low = a.new_low, o.close = a.new_close;