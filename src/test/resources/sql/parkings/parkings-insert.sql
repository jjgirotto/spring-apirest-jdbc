insert into users (id, username, password, role) values (101, 'juliana@gmail.com', '$2a$12$sNLoE6tXpr9i3deeODrxm.oqaKbekIAU6uEh1Bi20p/uY2O/fxsPS', 'ROLE_ADMIN');
insert into users (id, username, password, role) values (102, 'ana@gmail.com', '$2a$12$sNLoE6tXpr9i3deeODrxm.oqaKbekIAU6uEh1Bi20p/uY2O/fxsPS', 'ROLE_CLIENT');
insert into users (id, username, password, role) values (103, 'juli@gmail.com', '$2a$12$sNLoE6tXpr9i3deeODrxm.oqaKbekIAU6uEh1Bi20p/uY2O/fxsPS', 'ROLE_CLIENT');

insert into clients (id, name, cpf, id_user) values (22, 'Ana Carolina da Silva', '88110019005', 102);
insert into clients (id, name, cpf, id_user) values (23, 'Julieta', '55327006050', 103);

insert into spaces (id, code, status) values (100, 'A-01', 'OCCUPIED');
insert into spaces (id, code, status) values (200, 'A-02', 'OCCUPIED');
insert into spaces (id, code, status) values (300, 'A-03', 'OCCUPIED');
insert into spaces (id, code, status) values (400, 'A-04', 'FREE');
insert into spaces (id, code, status) values (500, 'A-05', 'FREE');

insert into clients_have_spaces (recipt_number, plate, brand, model, color, date_entry, id_client, id_space)
    values ('20230313-101300', 'FIT-1020', 'FIAT', 'PALIO', 'GREEN', '2023-03-13 10:13:00', 23, 100);
insert into clients_have_spaces (recipt_number, plate, brand, model, color, date_entry, id_client, id_space)
    values ('20230314-101400', 'SIE-1020', 'FIAT', 'SIENA', 'WHITE', '2023-03-14 10:14:00', 22, 200);
insert into clients_have_spaces (recipt_number, plate, brand, model, color, date_entry, id_client, id_space)
    values ('20230315-101500', 'FIT-1020', 'FIAT', 'PALIO', 'GREEN', '2023-03-15 10:15:00', 23, 300);