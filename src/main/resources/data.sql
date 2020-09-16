INSERT INTO socio (username, password, first_name, last_name, email, register_date, last_checkin_date, active)
VALUES ('js', '$2a$10$JQOfG5Tqnf97SbGcKsalz.XpDQbXi1APOf2SHPVW27bWNioi9nI8y', 'Johann Sebastian', 'Bach', 'jsbach@gmail.com', '2019-08-12 09:50:45', '2019-08-12 09:50:45', true);
INSERT INTO socio (username, password, first_name, last_name, email, register_date, last_checkin_date, active)
VALUES ('rw', '$2a$10$/2GVzZk6rGlnCLd45hYtneoqFMLJ0cS5O.5GBTb6x2ubCN.8CBpMO', 'Richard', 'Wagner', 'rwagner@gmail.com', '2019-08-12 09:50:45', '2019-08-12 09:50:45', true);
INSERT INTO socio (username, password, first_name, last_name, email, register_date, last_checkin_date, active)
VALUES ('bb', '$2a$10$/2GVzZk6rGlnCLd45hYtneoqFMLJ0cS5O.5GBTb6x2ubCN.8CBpMO', 'Bela', 'Bartok', 'bbartok@gmail.com', '2019-08-12 09:50:45', '2019-08-12 09:50:45', true);

INSERT INTO country (name, code) VALUES ('Netherlands','NL');
INSERT INTO country (name, code) VALUES ('Spain','ES');
INSERT INTO country (name, code) VALUES ('France','FR');
INSERT INTO country (name, code) VALUES ('Germany','DB');
INSERT INTO country (name, code) VALUES ('England','EN');

INSERT INTO language (name, code) VALUES ('Dutch','NL');
INSERT INTO language (name, code) VALUES ('Spanish','ES');
INSERT INTO language (name, code) VALUES ('France','FR');
INSERT INTO language (name, code) VALUES ('German','BD');
INSERT INTO language (name, code) VALUES ('English','EN');

INSERT INTO role (name) VALUES ('ROLE_ADMIN');
INSERT INTO role (name) VALUES ('ROLE_SOCIO');

INSERT INTO socio_language (socio_id, language_id) VALUES ('1','1');
INSERT INTO socio_language (socio_id, language_id) VALUES ('1','2');
INSERT INTO socio_language (socio_id, language_id) VALUES ('2','1');
INSERT INTO socio_language (socio_id, language_id) VALUES ('2','3');
INSERT INTO socio_language (socio_id, language_id) VALUES ('3','2');
INSERT INTO socio_language (socio_id, language_id) VALUES ('3','4');

INSERT INTO address (street, city, postalcode, province, country_id, description, address_type, socio_id) 
VALUES ('Edmondstraat 36','Heerlen', '5467CZ', 'Limburg', (SELECT id FROM country WHERE code='NL'), 'some story', 'HOME', 1);
INSERT INTO address (street, city, postalcode, province, country_id, description, address_type, socio_id) 
VALUES ('ZuidWest 36','Amsterdam', '53454CZ', 'Zuid Holland', (SELECT id FROM country WHERE code='NL'), 'some other story', 'HOME', 1);

INSERT INTO socio_associated_socio (socio_id, associated_socio_id, associated_socio_date, associated_socio_state) VALUES ('1','2','2019-08-12 09:50:45', 'PENDING');
INSERT INTO socio_associated_socio (socio_id, associated_socio_id, associated_socio_date, associated_socio_state) VALUES ('1','3','2019-08-12 09:50:45', 'PENDING');
