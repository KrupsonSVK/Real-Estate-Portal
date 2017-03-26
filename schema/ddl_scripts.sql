--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.6
-- Dumped by pg_dump version 9.5.6

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: realitny_portal_prototyp; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE realitny_portal_prototyp WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'sk_SK.UTF-8' LC_CTYPE = 'sk_SK.UTF-8';


ALTER DATABASE realitny_portal_prototyp OWNER TO postgres;

\connect realitny_portal_prototyp

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- Name: types; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE types AS ENUM (
    'Ponuka',
    'Dopyt',
    'Prenájom',
    'Predaj',
    'Byt',
    'Dom',
    'Garáž',
    'Garsónka',
    '1-izbový byt',
    '2-izbový byt',
    '3-izbový byt',
    '4-izbový byt a väčší',
    'Nový projekt',
    'Hotel',
    'Reštaurácia',
    'Chalupa',
    'Chata',
    'Kancelária',
    'Obchodný priestor',
    'Pozemok',
    'Sklad',
    'Hala',
    'Záhrada',
    'Ostatné'
);


ALTER TYPE types OWNER TO postgres;

--
-- Name: truncate_tables(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION truncate_tables() RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    statements CURSOR FOR
        SELECT tablename FROM pg_tables
        WHERE schemaname = 'public';
BEGIN
    FOR stmt IN statements LOOP	
	CONTINUE WHEN (quote_ident(stmt.tablename) = 'cities' OR quote_ident(stmt.tablename) = 'types'); 
        EXECUTE 'TRUNCATE TABLE ' || quote_ident(stmt.tablename) || ' CASCADE; ' || 'ALTER SEQUENCE ' || 
        (CASE WHEN (quote_ident(stmt.tablename) = 'buyer_searches' OR quote_ident(stmt.tablename) = 'seller_searches') 
        THEN 'searches' ELSE quote_ident(stmt.tablename) END) || '_id_seq RESTART';
    END LOOP;
END;
$$;


ALTER FUNCTION public.truncate_tables() OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: ads; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE ads (
    id integer NOT NULL,
    title character varying(256),
    price integer NOT NULL,
    description text,
    city_id integer NOT NULL,
    owner_id integer NOT NULL,
    created_at date NOT NULL
);


ALTER TABLE ads OWNER TO postgres;

--
-- Name: ads_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ads_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ads_id_seq OWNER TO postgres;

--
-- Name: ads_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ads_id_seq OWNED BY ads.id;


--
-- Name: searches; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE searches (
    id integer NOT NULL,
    phrase text,
    user_id integer NOT NULL
);


ALTER TABLE searches OWNER TO postgres;

--
-- Name: buyer_searches; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE buyer_searches (
)
INHERITS (searches);


ALTER TABLE buyer_searches OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE users (
    username character varying(256) NOT NULL,
    password character varying(256) NOT NULL,
    name character varying(256) NOT NULL,
    surname character varying(256) NOT NULL,
    email character varying(256) NOT NULL,
    phone bigint NOT NULL,
    id integer NOT NULL
);


ALTER TABLE users OWNER TO postgres;

--
-- Name: buyers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE buyers (
    id integer
)
INHERITS (users);


ALTER TABLE buyers OWNER TO postgres;

--
-- Name: buyers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE buyers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE buyers_id_seq OWNER TO postgres;

--
-- Name: buyers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE buyers_id_seq OWNED BY buyers.id;


--
-- Name: categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE categories (
    ad_id integer NOT NULL,
    id integer NOT NULL,
    type types
);


ALTER TABLE categories OWNER TO postgres;

--
-- Name: categories_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE categories_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE categories_id_seq OWNER TO postgres;

--
-- Name: categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE categories_id_seq OWNED BY categories.id;


--
-- Name: cities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE cities (
    id integer NOT NULL,
    name character varying(256),
    latitude double precision NOT NULL,
    longitude double precision NOT NULL
);


ALTER TABLE cities OWNER TO postgres;

--
-- Name: cities_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE cities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cities_id_seq OWNER TO postgres;

--
-- Name: cities_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE cities_id_seq OWNED BY cities.id;


--
-- Name: features; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE features (
    id integer NOT NULL,
    name character varying(256),
    ad_id integer NOT NULL,
    quantity smallint
);


ALTER TABLE features OWNER TO postgres;

--
-- Name: features_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE features_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE features_id_seq OWNER TO postgres;

--
-- Name: features_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE features_id_seq OWNED BY features.id;


--
-- Name: likes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE likes (
    id integer NOT NULL,
    liked_at date NOT NULL,
    buyer_id integer NOT NULL,
    ad_id integer NOT NULL
);


ALTER TABLE likes OWNER TO postgres;

--
-- Name: likes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE likes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE likes_id_seq OWNER TO postgres;

--
-- Name: likes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE likes_id_seq OWNED BY likes.id;


--
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE messages (
    subject character varying(256),
    content text,
    from_mail character varying(256) NOT NULL,
    to_mail character varying(256) NOT NULL,
    id integer NOT NULL
);


ALTER TABLE messages OWNER TO postgres;

--
-- Name: messages_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE messages_id_seq OWNER TO postgres;

--
-- Name: messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE messages_id_seq OWNED BY messages.id;


--
-- Name: photos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE photos (
    id integer NOT NULL,
    filename character varying(256),
    title character varying(256),
    description text,
    ad_id integer NOT NULL
);


ALTER TABLE photos OWNER TO postgres;

--
-- Name: photos_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE photos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE photos_id_seq OWNER TO postgres;

--
-- Name: photos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE photos_id_seq OWNED BY photos.id;


--
-- Name: searches_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE searches_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE searches_id_seq OWNER TO postgres;

--
-- Name: searches_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE searches_id_seq OWNED BY searches.id;


--
-- Name: seller_searches; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE seller_searches (
)
INHERITS (searches);


ALTER TABLE seller_searches OWNER TO postgres;

--
-- Name: sellers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE sellers (
    id integer,
    employee_id integer NOT NULL,
    company character varying(256) NOT NULL
)
INHERITS (users);


ALTER TABLE sellers OWNER TO postgres;

--
-- Name: sellers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sellers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sellers_id_seq OWNER TO postgres;

--
-- Name: sellers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE sellers_id_seq OWNED BY sellers.id;


--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ads ALTER COLUMN id SET DEFAULT nextval('ads_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY buyer_searches ALTER COLUMN id SET DEFAULT nextval('searches_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY buyers ALTER COLUMN id SET DEFAULT nextval('buyers_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY categories ALTER COLUMN id SET DEFAULT nextval('categories_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cities ALTER COLUMN id SET DEFAULT nextval('cities_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY features ALTER COLUMN id SET DEFAULT nextval('features_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY likes ALTER COLUMN id SET DEFAULT nextval('likes_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY messages ALTER COLUMN id SET DEFAULT nextval('messages_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY photos ALTER COLUMN id SET DEFAULT nextval('photos_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY searches ALTER COLUMN id SET DEFAULT nextval('searches_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY seller_searches ALTER COLUMN id SET DEFAULT nextval('searches_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sellers ALTER COLUMN id SET DEFAULT nextval('sellers_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Name: ads_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ads
    ADD CONSTRAINT ads_pkey PRIMARY KEY (id);


--
-- Name: buyers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY buyers
    ADD CONSTRAINT buyers_pkey PRIMARY KEY (id);


--
-- Name: categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (id);


--
-- Name: cities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cities
    ADD CONSTRAINT cities_pkey PRIMARY KEY (id);


--
-- Name: features_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY features
    ADD CONSTRAINT features_pkey PRIMARY KEY (id);


--
-- Name: likes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY likes
    ADD CONSTRAINT likes_pkey PRIMARY KEY (id);


--
-- Name: messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- Name: photos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY photos
    ADD CONSTRAINT photos_pkey PRIMARY KEY (id);


--
-- Name: searches_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY searches
    ADD CONSTRAINT searches_pkey PRIMARY KEY (id);


--
-- Name: sellers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sellers
    ADD CONSTRAINT sellers_pkey PRIMARY KEY (id);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: ads_city_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ads
    ADD CONSTRAINT ads_city_id_fkey FOREIGN KEY (city_id) REFERENCES cities(id);


--
-- Name: ads_owner_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ads
    ADD CONSTRAINT ads_owner_fkey FOREIGN KEY (owner_id) REFERENCES sellers(id);


--
-- Name: buyer_searches_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY buyer_searches
    ADD CONSTRAINT buyer_searches_user_id_fkey FOREIGN KEY (user_id) REFERENCES buyers(id);


--
-- Name: features_ad_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY features
    ADD CONSTRAINT features_ad_id_fkey FOREIGN KEY (ad_id) REFERENCES ads(id);


--
-- Name: likes_ad_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY likes
    ADD CONSTRAINT likes_ad_id_fkey FOREIGN KEY (ad_id) REFERENCES ads(id);


--
-- Name: likes_buyer_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY likes
    ADD CONSTRAINT likes_buyer_id_fkey FOREIGN KEY (buyer_id) REFERENCES buyers(id);


--
-- Name: photos_ad_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY photos
    ADD CONSTRAINT photos_ad_id_fkey FOREIGN KEY (ad_id) REFERENCES ads(id);


--
-- Name: seller_searches_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY seller_searches
    ADD CONSTRAINT seller_searches_user_id_fkey FOREIGN KEY (user_id) REFERENCES sellers(id);


--
-- Name: types_ad_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY categories
    ADD CONSTRAINT types_ad_id_fkey FOREIGN KEY (ad_id) REFERENCES ads(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

