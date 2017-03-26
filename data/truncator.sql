CREATE OR REPLACE FUNCTION truncate_tables() RETURNS void AS $$
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
$$ LANGUAGE plpgsql;

SELECT truncate_tables();