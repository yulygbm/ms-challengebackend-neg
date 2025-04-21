CREATE TABLE if not exists historialpeticion(
  id serial PRIMARY KEY,
  endpoint varchar(255),
  parametros varchar(255),
  respuesta varchar(255),
  fecha_creacion timestamp
);