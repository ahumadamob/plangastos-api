ALTER TABLE usuarios
    ADD COLUMN password_hash VARCHAR(255) NULL;

UPDATE usuarios
SET password_hash = '$2a$10$5Rn1Cj4lNBRH9f7DmqD5w.jMSKfQfN79nYpuL1L/J9JQfAq1xj4hC';

ALTER TABLE usuarios
    MODIFY COLUMN password_hash VARCHAR(255) NOT NULL;

ALTER TABLE presupuestos
    ADD COLUMN usuario_id BIGINT NULL;

ALTER TABLE partidas_planificadas
    ADD COLUMN usuario_id BIGINT NULL;

UPDATE presupuestos p
SET usuario_id = (
    SELECT u.id FROM usuarios u ORDER BY u.id LIMIT 1
)
WHERE p.usuario_id IS NULL;

UPDATE partidas_planificadas pp
SET usuario_id = (
    SELECT p.usuario_id FROM presupuestos p WHERE p.id = pp.presupuesto_id
)
WHERE pp.usuario_id IS NULL;

ALTER TABLE presupuestos
    MODIFY COLUMN usuario_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_presupuesto_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id);

ALTER TABLE partidas_planificadas
    MODIFY COLUMN usuario_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_partida_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id);

ALTER TABLE cuentas_financieras
    MODIFY COLUMN usuario_id BIGINT NOT NULL;

ALTER TABLE rubros
    MODIFY COLUMN usuario_id BIGINT NOT NULL;
