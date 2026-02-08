ALTER TABLE partidas_planificadas
    ADD COLUMN partida_origen_id BIGINT NULL;

ALTER TABLE partidas_planificadas
    ADD CONSTRAINT fk_partidas_planificadas_partida_origen
        FOREIGN KEY (partida_origen_id) REFERENCES partidas_planificadas (id);

CREATE INDEX idx_partidas_planificadas_partida_origen_id
    ON partidas_planificadas (partida_origen_id);
