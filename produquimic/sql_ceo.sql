-- Table: sri_detalle_comprobante

-- DROP TABLE sri_detalle_comprobante;

CREATE TABLE sri_detalle_comprobante
(
  ide_srdec integer NOT NULL,
  ide_srcom integer,
  codigo_principal_srdec character varying(25) NOT NULL,
  codigo_auxiliar_srdec character varying(25),
  descripcion_srdec character varying(250) NOT NULL,
  cantidad_srdec money NOT NULL,
  precio_srdec money NOT NULL,
  descuento_detalle_srdec money,
  total_detalle_srdec money NOT NULL,
  porcentaje_iva_srdec money NOT NULL,
  CONSTRAINT pk_sri_detalle_comprobante PRIMARY KEY (ide_srdec),
  CONSTRAINT fk_sri_deta_relations_sri_comp FOREIGN KEY (ide_srcom)
      REFERENCES sri_comprobante (ide_srcom) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sri_detalle_comprobante
  OWNER TO postgres;

-- Index: relationshipfe_2_fk

-- DROP INDEX relationshipfe_2_fk;

CREATE INDEX relationshipfe_2_fk
  ON sri_detalle_comprobante
  USING btree
  (ide_srcom);

-- Index: sri_detalle_comprobante_pk

-- DROP INDEX sri_detalle_comprobante_pk;

CREATE UNIQUE INDEX sri_detalle_comprobante_pk
  ON sri_detalle_comprobante
  USING btree
  (ide_srdec);



-- Table: sri_comprobante

-- DROP TABLE sri_comprobante;

CREATE TABLE sri_comprobante
(
  ide_srcom integer NOT NULL,
  ide_srfid smallint,
  ide_sresc smallint,
  coddoc_srcom character varying(2) NOT NULL,
  tipoemision_srcom character varying(1),
  claveacceso_srcom character varying(50),
  secuencial_srcom character varying(9) NOT NULL,
  fechaemision_srcom date NOT NULL,
  autorizacion_srcomn character varying(100),
  fechaautoriza_srcom date,
  estab_srcom character varying(3),
  ptoemi_srcom character varying(3),
  fecha_sistema_srcom date,
  ip_genera_srcom character varying(25),
  subtotal_srcom money,
  subtotal0_srcom money,
  base_grabada_srcom money,
  iva_srcom money,
  descuento_srcom money,
  total_srcom money NOT NULL,
  forma_cobro_srcom character varying(2),
  identificacion_srcom character varying(20),
  codigo_docu_mod_srcom character varying(2),
  num_doc_mod_srcom character varying(20),
  fecha_emision_mod_srcom date,
  valor_mod_srcom money,
  motivo_srcom character varying(200),
  num_intentos_envio_srcom integer,
  envio_mail_srcom boolean,
  en_nube_srcom boolean,
  ide_geper integer,
  ide_cntdo integer,
  ide_empr integer,
  ide_sucu integer,
  CONSTRAINT pk_sri_comprobante PRIMARY KEY (ide_srcom),
  CONSTRAINT fk_sri_comp_relations_sri_esta FOREIGN KEY (ide_sresc)
      REFERENCES sri_estado_comprobante (ide_sresc) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT,
  CONSTRAINT fk_sri_comp_relations_sri_firm FOREIGN KEY (ide_srfid)
      REFERENCES sri_firma_digital (ide_srfid) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT
)


