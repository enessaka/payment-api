CREATE DATABASE IF NOT EXISTS  payments;
USE payments;

CREATE TABLE  IF NOT EXISTS accounts(
    iban VARCHAR(34) PRIMARY KEY,
    status VARCHAR(1), -- S: Suspended, A: Active, T: Terminated
    balance DECIMAL,
    account_management_system VARCHAR(3) -- P7, RAI
);

CREATE TABLE  IF NOT EXISTS payments(
    id VARCHAR(20) PRIMARY KEY,
    status VARCHAR(1), -- P: Inprogress, R: Rejected, A: Accepted
    debtor_iban VARCHAR(34),
    creditor_iban VARCHAR(34),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL
);

insert into accounts values ('NL000000000000001', 'A', 20, 'P7');
insert into accounts values ('NL000000000000002', 'A', 30, 'RAI');
insert into accounts values ('NL000000000000004', 'S', 0,  'RAI');