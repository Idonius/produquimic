---Catalogo de caracteristicas  de articulos
CREATE TABLE inv_caracteristica
(
    ide_incar int NOT NULL,
    nombre_incar  character varying(60),
CONSTRAINT pk_inv_caractristica PRIMARY KEY (ide_incar)
);

---Catalogo de Areas de aplicación  de articulos
CREATE TABLE inv_area
(
    ide_inare int NOT NULL,
    nombre_inare   character varying(60),
CONSTRAINT pk_inv_area PRIMARY KEY (ide_inare)
);


CREATE TABLE inv_articulo_carac
(
    ide_inarc int NOT NULL,
    ide_inarti int,
    ide_incar int,
    ide_inare int,
    detalle_inarc   text,
CONSTRAINT pk_inv_articulo_carac PRIMARY KEY (ide_inarc),
CONSTRAINT fk_inv_arti_reference_inv_caract FOREIGN KEY (ide_incar)
      REFERENCES inv_caracteristica (ide_incar) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT fk_inv_arti_reference_inv_area FOREIGN KEY (ide_inare)
      REFERENCES inv_area (ide_inare) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT fk_inv_arti_reference_inv_articulo FOREIGN KEY (ide_inarti)
      REFERENCES inv_articulo (ide_inarti) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);