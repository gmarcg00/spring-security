insert into customers (email, pwd) VALUES
  ('account@debuggeandoieas.com', 'to_be_encoded'),
  ('cards@debuggeandoieas.com', 'to_be_encoded'),
  ('loans@debuggeandoieas.com', 'to_be_encoded'),
  ('balance@debuggeandoieas.com', 'to_be_encoded');

insert into roles (role_name, description, id_customer) VALUES
    ('ROLE_ADMIN', 'can not view account endpoint',1),
    ('ROLE_ADMIN', 'can not view cards endpoint',2),
    ('ROLE_USER', 'can not view loans endpoint',3),
    ('ROLE_USER', 'can not view balance endpoint',4);