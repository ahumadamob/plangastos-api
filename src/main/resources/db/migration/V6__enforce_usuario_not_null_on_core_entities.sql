SET @legacy_user_email := 'legacy.migration@plangastos.local';
SET @legacy_user_password_hash := '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjQ4L9Q5f5lHppZArYrusS4x2QVFG4u';

INSERT INTO usuarios (created_at, updated_at, email, password_hash, activo)
SELECT NOW(6), NOW(6), @legacy_user_email, @legacy_user_password_hash, b'0'
WHERE NOT EXISTS (SELECT 1 FROM usuarios LIMIT 1);

SET @fallback_usuario_id := (SELECT MIN(id) FROM usuarios);

SET @presupuestos_usuario_column_exists := (
    SELECT COUNT(*)
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
      AND table_name = 'presupuestos'
      AND column_name = 'usuario_id'
);
SET @presupuestos_usuario_column_sql := IF(
    @presupuestos_usuario_column_exists = 0,
    'ALTER TABLE presupuestos ADD COLUMN usuario_id BIGINT NULL',
    'SELECT 1'
);
PREPARE stmt FROM @presupuestos_usuario_column_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @partidas_usuario_column_exists := (
    SELECT COUNT(*)
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
      AND table_name = 'partidas_planificadas'
      AND column_name = 'usuario_id'
);
SET @partidas_usuario_column_sql := IF(
    @partidas_usuario_column_exists = 0,
    'ALTER TABLE partidas_planificadas ADD COLUMN usuario_id BIGINT NULL',
    'SELECT 1'
);
PREPARE stmt FROM @partidas_usuario_column_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

UPDATE cuentas_financieras cf
LEFT JOIN usuarios u ON u.id = cf.usuario_id
SET cf.usuario_id = @fallback_usuario_id
WHERE cf.usuario_id IS NULL OR u.id IS NULL;

UPDATE rubros r
LEFT JOIN usuarios u ON u.id = r.usuario_id
SET r.usuario_id = @fallback_usuario_id
WHERE r.usuario_id IS NULL OR u.id IS NULL;

UPDATE presupuestos p
LEFT JOIN usuarios u ON u.id = p.usuario_id
SET p.usuario_id = @fallback_usuario_id
WHERE p.usuario_id IS NULL OR u.id IS NULL;

UPDATE partidas_planificadas pp
LEFT JOIN usuarios u ON u.id = pp.usuario_id
SET pp.usuario_id = @fallback_usuario_id
WHERE pp.usuario_id IS NULL OR u.id IS NULL;

ALTER TABLE cuentas_financieras MODIFY COLUMN usuario_id BIGINT NOT NULL;
ALTER TABLE rubros MODIFY COLUMN usuario_id BIGINT NOT NULL;
ALTER TABLE presupuestos MODIFY COLUMN usuario_id BIGINT NOT NULL;
ALTER TABLE partidas_planificadas MODIFY COLUMN usuario_id BIGINT NOT NULL;

SET @fk_presupuestos_usuario_exists := (
    SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE table_schema = DATABASE()
      AND table_name = 'presupuestos'
      AND constraint_name = 'fk_presupuestos_usuario'
      AND constraint_type = 'FOREIGN KEY'
);
SET @fk_presupuestos_usuario_sql := IF(
    @fk_presupuestos_usuario_exists = 0,
    'ALTER TABLE presupuestos ADD CONSTRAINT fk_presupuestos_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT ON UPDATE RESTRICT',
    'SELECT 1'
);
PREPARE stmt FROM @fk_presupuestos_usuario_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_partidas_usuario_exists := (
    SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE table_schema = DATABASE()
      AND table_name = 'partidas_planificadas'
      AND constraint_name = 'fk_partidas_planificadas_usuario'
      AND constraint_type = 'FOREIGN KEY'
);
SET @fk_partidas_usuario_sql := IF(
    @fk_partidas_usuario_exists = 0,
    'ALTER TABLE partidas_planificadas ADD CONSTRAINT fk_partidas_planificadas_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT ON UPDATE RESTRICT',
    'SELECT 1'
);
PREPARE stmt FROM @fk_partidas_usuario_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
