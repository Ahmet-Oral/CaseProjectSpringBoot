import numpy as np
import random
from faker import Faker
from datetime import datetime
import pymysql.cursors

# ----------- FOR 3.000.000 ROWS DATA GENERATION, TABLE CREATION, INDEXING AND UPLOAD TAKES AROUND 200 SECONDS -----------------

# MySQL
hostname = 'localhost'
database = 'spring_task'
user = 'root'
password = 'password'
numberOfRowsToGenerate = 3000000
# tableName = 'weather_data' - do not change

# -------------------------------------------------------------------------


start_date = datetime.strptime('11-05-1960', '%d-%m-%Y')
end_date = datetime.strptime('11-05-2022', '%d-%m-%Y')
fake = Faker()


def getRandomDate():
    return datetime.strptime(str(fake.date_between(start_date=start_date, end_date=end_date)), '%Y-%m-%d')


conditions = np.array(["Sunny", "Cloudy", "Rainy", "Foggy", "Windy", "Snowy", "Clear", "Partially Sunny"])
cities = np.array([["Bursa", "İstanbul", "Ankara"],
                   ["Amsterdam", "Eindhoven", "Rotterdam"],
                   ["Trondheim", "Stavanger", "Bergen"],
                   ["Toronto", "Vancouver", "Victoria"],
                   ["London", "Liverpool", "Bristol"]])
countries = np.array(["Turkey", "Holland", "Norway", "Canada", "England"])

commit = []
for i in range(1, numberOfRowsToGenerate + 1):
    temp = random.randint(-20, 55)
    r1 = random.randint(0, 2)
    r2 = random.randint(0, 2)
    cond = random.randint(0, 7)
    # id, temp, country, city, time, condition
    # data = np.concatenate((data, [i, temp, countries[r1], cities[r1][r2], getRandomDate(), conditions[cond]]))
    commit.append((i, temp, countries[r1], cities[r1][r2], getRandomDate(), conditions[cond]))

print("Data generated, uploading now")
try:
    connection = pymysql.connect(host=hostname,
                                 database=database,
                                 user=user,
                                 password=password)

    mySql_insert_query = """INSERT INTO weather_data (id, temperature, country, city, date_, condition_)
    VALUES (%s, %s, %s, %s, %s, %s) """

    index_query1 = """create index IX_temp on weather_data(temperature)"""
    index_query2 = """create index IX_country on weather_data(country)"""
    index_query3 = """create index IX_city on weather_data(city)"""
    index_query4 = """create index IX_condition on weather_data(condition_)"""
    index_query5 = """create index IX_date on weather_data(date_)"""

    cursor = connection.cursor()

    print("created table!, now uploading data")
    cursor.executemany(mySql_insert_query, tuple(commit))

    connection.commit()
    print("Records successfully inserted  into 'weather_data' table!")
except pymysql.connect().Error as error:
    print("Failed to insert record into MySQL table {}".format(error))
