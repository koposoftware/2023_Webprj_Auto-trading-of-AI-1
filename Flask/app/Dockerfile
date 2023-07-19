FROM python:3.11

WORKDIR /app

COPY . /app

RUN apt-get update && apt-get install -y python3-dev default-libmysqlclient-dev

RUN pip3 install --upgrade werkzeug

RUN pip3 install --no-cache-dir -r requirements.txt

CMD ["python3", "app.py"]

