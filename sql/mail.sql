CREATE TABLE mail_message (
  id identity, 
  mail_folder_id int NOT NULL,
  state int NOT NULL,
  subject varchar(2048) NOT NULL,
  from_addr varchar(256) NOT NULL,
  message_id varchar(512) NOT NULL,
  sent_date timestamp NOT NULL,
  received_date timestamp NOT NULL,
  base_content varchar(65536) NOT NULL,
  mime_content_type varchar(256) NOT NULL,
  raw_message blob NOT NULL,
  ext_info varchar(256) NOT NULL
);

CREATE TABLE mail_message_field  (
  id identity, 
  mail_message_id int NOT NULL,
  field_type int NOT NULL,
value varchar(512) NOT NULL,
);

