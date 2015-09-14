
CREATE TABLE contact (
  id identity, 
  contacts_folder_id int NOT NULL,
  title varchar(2048) NOT NULL,
  notes varchar(65536) NOT NULL
);

CREATE TABLE contact_value (
  id identity, 
  contact_id int NOT NULL,
type_id int NOT NULL,
  value varchar(2048) NOT NULL,
preferable bool NOT NULL
);


