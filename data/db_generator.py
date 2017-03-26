#!/usr/bin/python
# -*- coding: utf-8 -*-

import time, random, psycopg2, lorem
from dateutil import parser

#setup connection
hostname = 'localhost'
username = 'postgres'
password = 'postgres'
database = 'realitny_portal_prototyp'

#number of rows
buyers = 500000
sellers = 10000
users = buyers + sellers
ads = 30 * sellers
cities = 33
likes = 20 * buyers
searches = 100 * users
messages = 5 * users

#source words
name_dictionary={
    'pre':['A','Di','Mo','Fa','Ko','Ma','To','Pa','Pe','Ta','Sa','O','I','E','U','Da','Ba','De','Ku','Vla','Ka','Na','Ja','Jo','Kri','Ki','Ka','Ni'],
    'post':['dar','kil','glar','tres','van','kol','lar','ko','ter','mas','zuz','no','sa','muel','bdul','dimir','rtin','hn','th','na','vana','bora']}
    
mail_dictionary={
    'second_domain':['cloud','mail','post','box','posta','webmail','freemail','outlook','office','list'],
    'top_domain':['sk','cz','com','eu','org']}
    
company_dictionary={
    'pre':['Ana','Viva','Micro','Rea','Tops','Xana','Macro','Giga','Rado','Fixa','Nano','Orga','Eco','Logi','Ele','Niko','Alfa','Grange'],
    'post':['xiss','rod','sax','corp','tory','opan','bora','graf','store','borg','step','star','soft','stic','tric','']}

ad_type = ['Ponuka','Dopyt']

ad_category = ['Predaj','Prenájom']

ad_subcategory = ['Byt', 'Dom', 'Garáž','Nový projekt','Hotel','Reštaurácia','Chalupa','Chata','Kancelária','Obchodný priestor','Pozemok','Sklad','Hala','Záhrada','Ostatné']

ad_subsubcategory = ['Garsónka','1-izbový byt','2-izbový byt','3-izbový byt','4-izbový byt a väčší','Nový projekt','Ostatné']

features = ['Klimatizácia', 'Garáž', 'Internet','Pevná linka','Zariadený','Alarm','Fotovoltaické panely','Solárne kolektory','Studňa','WC','Kúpeľňa','Bazén','Sauna','Altánok','Gril','Pivnica', 'Balkón', 'Terasa','Lodžia','Podkrovie','Povala','Parkovisko','Skleník','Kanalizácia']


# random date generator
def randDate(start):    
    
    format = '%Y-%m-%d'
    end = time.strftime(format)
    prop = random.random()
    
    start_time = time.mktime(time.strptime(start, format))
    end_time = time.mktime(time.strptime(end, format))
    rand_time = start_time + prop * (end_time - start_time)

    return time.strftime(format, time.localtime(rand_time))


# weighted chooser used for optimal item distribution
def weightedChoice(choices):  
    
   total = sum(weigth for choice, weigth in choices)
   rand = random.uniform(0, total)
   
   for choice, weigth in choices:
      rand -= weigth    
      if rand <= 0:
         return choice


# routine to run a query on a database
def generateQueries( conn ) :
    cur = conn.cursor()

    #populate sellers
    for i in xrange(sellers):
        
        company = company_dictionary['pre'][random.randint(0,len(company_dictionary['pre'])-1)] + company_dictionary['post'][random.randint(0,len(company_dictionary['post'])-1)]
    
        name = name_dictionary['pre'][random.randint(0,len(name_dictionary['pre'])-1)] + name_dictionary['post'][random.randint(0,len(name_dictionary['post'])-1)]
        surname = name_dictionary['pre'][random.randint(0,len(name_dictionary['pre'])-1)] + name_dictionary['post'][random.randint(0,len(name_dictionary['post'])-1)]
        username = (name + surname).lower() + "_" + company.lower()
        password = name.upper()              
        
        second_domain = company.lower()
        top_domain = mail_dictionary['top_domain'][random.randint(0,len(mail_dictionary['top_domain'])-1)]
        domain = (second_domain + "." + top_domain).lower()
        email= (name + surname).lower() + "@" + domain
        
        phone= str(42190) + str("%0.7d" % random.randint(0,9999999))
        
        employee_id = str("%0.7d" % random.randint(0,9999999))
        
        cur.execute( "INSERT INTO sellers(username,password,name,surname,email,phone,employee_id,company) VALUES (\'" + username + "\',\'" + password + "\',\'" + name + "\',\'" + surname + "\',\'" + email + "\'," + phone + "," + employee_id + ",\'" + company + "\');" )
   
    db_connection.commit()

    #populate buyers
    for i in xrange(buyers):
    
        name = name_dictionary['pre'][random.randint(0,len(name_dictionary['pre'])-1)] + name_dictionary['post'][random.randint(0,len(name_dictionary['post'])-1)]
        surname = name_dictionary['pre'][random.randint(0,len(name_dictionary['pre'])-1)] + name_dictionary['post'][random.randint(0,len(name_dictionary['post'])-1)]
        username = (name + surname).lower() + str(random.randint(50,99)) 
        password = name.upper()
                
        second_domain = mail_dictionary['second_domain'][random.randint(0,len(mail_dictionary['second_domain'])-1)]
        top_domain = mail_dictionary['top_domain'][random.randint(0,len(mail_dictionary['top_domain'])-1)]
        domain = (second_domain + "." + top_domain).lower()
        email= username + "@" + domain
        
        phone= str(42190) + str("%0.7d" % random.randint(0,9999999))
        
        cur.execute( "INSERT INTO buyers(username,password,name,surname,email,phone) VALUES (\'" + username + "\',\'" + password + "\',\'" + name + "\',\'" + surname + "\',\'" + email + "\'," + phone + ");" )

    db_connection.commit()

    #populate ads
    for i in xrange(ads):
    
        title = lorem.sentence()
        price = str(weightedChoice([(0,0.1),(random.randint(10,200),0.6),(random.randint(200,500),0.2),(random.randint(500,9999),0.1)]) * 1000)
        description = str('_'.join(str(lorem.sentence()) for i in xrange(random.randint(0,20))))        
        created_at = randDate(start = "2015-1-1")
    
        city_id = str(random.randint(1,cities))
        owner_id = str(random.randint(1,sellers))
        
        cur.execute( "INSERT INTO ads(title,price,description,created_at,city_id,owner_id) VALUES (\'" + title + "\'," + price + ",\'" + description + "\',\'" + created_at + "\'," + city_id + "," + owner_id + ");" )
      
    db_connection.commit()

    #populate likes
    for i in xrange(likes):    
            
        buyer_id = str(random.randint(1,buyers))
        ad_id = str(random.randint(1,ads))
        
        #find date of creation of ad and generate random date of like
        cur.execute( "SELECT created_at FROM ads WHERE id = " + ad_id )
        date = str(cur.fetchone())
        liked_at =  str(randDate(start = str(parser.parse(date[15:-3]).date())))
        
        cur.execute( "INSERT INTO likes(buyer_id,ad_id,liked_at) VALUES (" + buyer_id + "," + ad_id + ",\'"  + liked_at + "\');")

    db_connection.commit()

    #populate searches
    for i in xrange(searches):    
            
        table = str(weightedChoice([("buyer_searches",0.8),("seller_searches",0.2)]))
        user_id = str(random.randint( 1 ,( buyers if table == "buyer_searches" else sellers )))
        phrase = lorem.sentence().lower()
        
        cur.execute( "INSERT INTO " + table + "(user_id,phrase) VALUES (" + user_id + ",\'" + phrase + "\')" )

    db_connection.commit()

    #populate messages
    for i in xrange(searches):
                
        from_type = str(weightedChoice([("buyers",0.4),("sellers",0.6)]))
        to_type = str(weightedChoice([("buyers",0.6),("sellers",0.4)]))    
        
        from_id = str(random.randint( 1 ,( buyers if from_type == "buyers" else sellers )))
        to_id = str(random.randint( 1 ,( buyers if to_type == "buyers" else sellers )))
        
        #find email of user
        cur.execute( "SELECT email FROM " + from_type + " WHERE id = " + from_id )        
        from_mail =  str(cur.fetchone())[2:-3]
        
        cur.execute( "SELECT email FROM " + to_type + " WHERE id = " + to_id )        
        to_mail =  str(cur.fetchone())[2:-3]   
        
        subject = lorem.sentence()
        content = str('_'.join(str(lorem.sentence()) for i in xrange(random.randint(0,20))))
        
        cur.execute( "INSERT INTO messages(from_mail,to_mail,subject,content) VALUES (\'" + from_mail + "\',\'" + to_mail + "\',\'" + subject + "\',\'" + content + "\')" )

    db_connection.commit()

    #populate categories
    for ad_id in xrange(ads):
        
        type = str(random.choice(ad_type))
        cur.execute( "INSERT INTO categories(type,ad_id) VALUES (\'" + type + "\'," + str(ad_id+1) + ")" )

        category = str(random.choice(ad_category))
        cur.execute( "INSERT INTO categories(type,ad_id) VALUES (\'" + category + "\'," + str(ad_id+1) + ")" )
        
        subcategory = str(random.choice(ad_subcategory))        
        cur.execute( "INSERT INTO categories(type,ad_id) VALUES (\'" + subcategory + "\'," + str(ad_id+1) + ")" )

        if subcategory=="Byt":
            subsubcategory = str(random.choice(ad_subsubcategory))                
            cur.execute( "INSERT INTO categories(type,ad_id) VALUES (\'" + subsubcategory + "\'," + str(ad_id+1) + ")" )

    db_connection.commit()

    #populate features
    for ad_id in xrange(ads):
        
        # add random features for each ad
        for i in xrange(random.randint(1,10)):
            
            feature = str(random.choice(features))
            quantity = str(weightedChoice([(1,0.8),(random.randint(1,10),0.2)]))
            cur.execute( "INSERT INTO features(name,quantity,ad_id) VALUES (\'" + feature + "\'," + quantity + "," + str(ad_id+1) + ")" )
   
    db_connection.commit()

    #populate photos
    for ad_id in xrange(ads):
        
        gallery_size=20
        photos = random.randint(0,gallery_size)
        indexes = random.sample(range(0, gallery_size), photos)
        
        # add random photos for each ad
        for i in xrange(photos):
            filename = str( "/img" + str(indexes[i]) + ".png")
            description = lorem.sentence()    

            cur.execute( "INSERT INTO photos(filename,description,ad_id) VALUES (\'" + filename + "\',\'" + description + "\'," + str(ad_id+1) + ")" )                
    
    db_connection.commit()
        
            
# connecting postgresql database using psycopg2 
db_connection = psycopg2.connect( host=hostname, user=username, password=password, dbname=database )
#db_connection.autocommit = True
generateQueries( db_connection )
db_connection.close()
