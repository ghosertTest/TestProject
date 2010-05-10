CREATE TABLE NOTE
(
  id        CHAR(32),
  status    INTEGER(1),
  title     VARCHAR2(100),
  content   VARCHAR2(1000),
  post_time DATE,
  author    VARCHAR2(50),
  email     VARCHAR2(50)
);

ALTER TABLE NOTE
  ADD CONSTRAINt NOTE_ID_PK PRIMARY KEY(id);

CREATE TABLE REPLYNOTE
(
  id        CHAR(32),
  note_id   CHAR(32),
  content   VARCHAR2(1000),
  post_time DATE,
  author    VARCHAR2(50),
  email     VARCHAR2(50)
);

ALTER TABLE REPLYNOTE
  ADD CONSTRAINT REPLYNOTE_ID_PK PRIMARY KEY (id);

ALTER TABLE REPLYNOTE
  ADD CONSTRAINT REPLYNOTE_NOTE_ID_FK FOREIGN KEY (note_id)
  REFERENCES NOTE (id);