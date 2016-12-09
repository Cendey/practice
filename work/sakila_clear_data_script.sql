/* prevent some editor to add bom header in this sql script file */

/* deleted table group[address], and those are related */
delete from  payment;
delete from  rental;
delete from  customer;
delete from  inventory;
delete from  store;
delete from  staff;
delete from  address;

/* deleted table group[language], and those are related */
delete from  film_actor;
delete from  film_category;
delete from  film;
delete from  language;

/* deleted table group[actor], and those are related */
delete from  actor;

/* deleted table group[category], and those are related */
delete from  category;

/* deleted table group[country], and those are related */
delete from  city;
delete from  country;

/* deleted other table group as follows */
delete from  film_text;

/* deleted audit log tables */
delete from  actorcl;
delete from  addresscl;
delete from  categorycl;
delete from  citycl;
delete from  countrycl;
delete from  customercl;
delete from  filmcl;
delete from  film_actorcl;
delete from  film_categorycl;
delete from  film_textcl;
delete from  inventorycl;
delete from  languagecl;
delete from  paymentcl;
delete from  rentalcl;
delete from  staffcl;
delete from  storecl;

commit;
