INSERT INTO USERS(user_id, date_of_birth, tm_created, tm_last_update, email, gov_id, last_update_by, name, password)
VALUES(1, '1992-07-11', parsedatetime('22-01-2022 18:27:52', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('23-12-2022 17:41:51', 'dd-MM-yyyy HH:mm:ss'), 'user1@test.com', 'D41234', 'adminuser', 'John', 'test');

INSERT INTO USERS(user_id, date_of_birth, tm_created, tm_last_update, email, gov_id, last_update_by, name, password)
VALUES(2, '1998-08-08', parsedatetime('22-01-2023 18:27:52', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('23-12-2023 17:41:51', 'dd-MM-yyyy HH:mm:ss'), 'user2@test.com', 'D41234', 'adminuser', 'Kevin', 'test');

INSERT INTO USERS(user_id, date_of_birth, tm_created, tm_last_update, email, gov_id, last_update_by, name, password)
VALUES(3, '1997-08-08', parsedatetime('22-01-2023 18:27:52', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('23-12-2023 17:41:51', 'dd-MM-yyyy HH:mm:ss'), 'user3@test.com', 'D41234', 'adminuser', 'Andrew', 'test');

INSERT INTO PROPERTIES(property_id, capacity, admin_user_id, tm_created, tm_last_update, address, last_update_by)
VALUES(1, '10', 1, parsedatetime('22-01-2023 18:27:52', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('23-12-2023 17:41:51', 'dd-MM-yyyy HH:mm:ss'), 'San Francisco, California 94158-0297, US', 'adminuser');

INSERT INTO PROPERTIES(property_id, capacity, admin_user_id, tm_created, tm_last_update, address, last_update_by)
VALUES(2, '4', 1, parsedatetime('22-01-2023 18:27:52', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('23-12-2023 17:41:51', 'dd-MM-yyyy HH:mm:ss'), 'San Francisco, California 94158-0298, US', 'adminuser');

INSERT INTO PROPERTIES(property_id, capacity, admin_user_id, tm_created, tm_last_update, address, last_update_by)
VALUES(3, '10', 2, parsedatetime('22-01-2023 18:27:52', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('23-12-2023 17:41:51', 'dd-MM-yyyy HH:mm:ss'), 'San Francisco, California 94158-0299, US', 'adminuser');

INSERT INTO BOOKINGS(booking_id, type, checkin_date, checkout_date, property_id, tm_created, tm_last_update, user_id, last_update_by)
VALUES('4e185894-379a-4cf0-8a96-07e122344f9e', 'BOOK', '2023-01-22', '2023-01-25', 1, parsedatetime('22-01-2023 18:27:52', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('23-12-2023 17:41:51', 'dd-MM-yyyy HH:mm:ss'), 3, 'adminuser');

INSERT INTO BOOKINGS(booking_id, type, checkin_date, checkout_date, property_id, tm_created, tm_last_update, user_id, last_update_by)
VALUES('cb1a80bf-78a4-429b-974d-e1812a39cd63', 'BOOK', '2023-02-22', '2023-02-25', 1, parsedatetime('22-01-2023 18:27:52', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('23-12-2023 17:41:51', 'dd-MM-yyyy HH:mm:ss'), 2, 'adminuser');

INSERT INTO BOOKINGS(booking_id, type, checkin_date, checkout_date, property_id, tm_created, tm_last_update, user_id, last_update_by)
VALUES('16114191-bc75-47f2-b096-046939057941', 'BOOK', '2023-03-22', '2023-03-25', 1, parsedatetime('22-01-2023 18:27:52', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('23-12-2023 17:41:51', 'dd-MM-yyyy HH:mm:ss'), 2, 'adminuser');

INSERT INTO  BOOKINGS(booking_id, type, checkin_date, checkout_date, property_id, tm_created, tm_last_update, user_id, last_update_by)
VALUES('408e5c73-a9b8-4b27-bea0-597c45edafb4', 'BOOK', '2023-04-22', '2023-04-25', 1, parsedatetime('22-01-2023 18:27:52', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('23-12-2023 17:41:51', 'dd-MM-yyyy HH:mm:ss'), 2, 'adminuser');

INSERT INTO BOOKINGS(booking_id, type, checkin_date, checkout_date, property_id, tm_created, tm_last_update, user_id, last_update_by)
VALUES('658d6404-1b83-49e7-859f-75d38068bdc1', 'BOOK', '2023-05-22', '2023-05-25', 2, parsedatetime('22-01-2023 18:27:52', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('23-12-2023 17:41:51', 'dd-MM-yyyy HH:mm:ss'), 2, 'adminuser');

INSERT INTO BOOKINGS(booking_id, type, checkin_date, checkout_date, property_id, tm_created, tm_last_update, user_id, last_update_by)
VALUES('937ccc4a-240a-4aa2-95f7-8a95ed00554c', 'BOOK', '2023-02-22', '2023-02-25', 2, parsedatetime('22-01-2023 18:27:52', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('23-12-2023 17:41:51', 'dd-MM-yyyy HH:mm:ss'), 3, 'adminuser');

INSERT INTO BOOKINGS(booking_id, type, checkin_date, checkout_date, property_id, tm_created, tm_last_update, user_id, last_update_by)
VALUES('4624f10a-0e05-4dfd-a9b8-f27b882e7c13', 'BOOK', '2023-03-22', '2023-03-25', 2, parsedatetime('22-01-2023 18:27:52', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('23-12-2023 17:41:51', 'dd-MM-yyyy HH:mm:ss'), 3, 'adminuser');

INSERT INTO BOOKINGS(booking_id, type, checkin_date, checkout_date, property_id, tm_created, tm_last_update, user_id, last_update_by)
VALUES('3ff6c924-880e-4fd8-903c-418587f2887c', 'BOOK', '2023-04-22', '2023-04-25', 2, parsedatetime('22-01-2023 18:27:52', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('23-12-2023 17:41:51', 'dd-MM-yyyy HH:mm:ss'), 3, 'adminuser');

INSERT INTO BOOKINGS(booking_id, type, checkin_date, checkout_date, property_id, tm_created, tm_last_update, user_id, last_update_by)
VALUES('bda6dd43-fd54-4a5e-ac86-78bc2fb0c21f', 'BOOK', '2022-03-22', '2022-03-25', 3, parsedatetime('22-01-2023 18:27:52', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('23-12-2023 17:41:51', 'dd-MM-yyyy HH:mm:ss'), 1, 'adminuser');

INSERT INTO BOOKINGS(booking_id, type, checkin_date, checkout_date, property_id, tm_created, tm_last_update, user_id, last_update_by)
VALUES('20b87ced-9108-47a8-abcc-3c33dcb0a254', 'BOOK', '2022-04-22', '2022-04-25', 3, parsedatetime('22-01-2023 18:27:52', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('23-12-2023 17:41:51', 'dd-MM-yyyy HH:mm:ss'), 3, 'adminuser');

INSERT INTO TBL_ROLES(id, label)
VALUES(1, 'ROLE_NORMAL');

INSERT INTO TBL_ROLES(id, label)
VALUES(2, 'ROLE_OWNER');

INSERT INTO TBL_ROLES(id, label)
VALUES(3, 'ROLE_ADMIN');

INSERT INTO USER_ROLE(user_id, role_id)
VALUES(1, 1);

INSERT INTO USER_ROLE(user_id, role_id)
VALUES(2, 1);

INSERT INTO USER_ROLE(user_id, role_id)
VALUES(2, 2);

INSERT INTO USER_ROLE(user_id, role_id)
VALUES(3, 1);

INSERT INTO USER_ROLE(user_id, role_id)
VALUES(3, 2);

INSERT INTO USER_ROLE(user_id, role_id)
VALUES(3, 3);