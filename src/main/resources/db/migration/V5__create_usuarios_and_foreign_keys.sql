CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id)
);

SET @usuarios_email_unique_exists := (
    SELECT COUNT(*)
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
      AND table_name = 'usuarios'
      AND index_name = 'uk_usuarios_email'
);
SET @usuarios_email_unique_sql := IF(
    @usuarios_email_unique_exists = 0,
    'ALTER TABLE usuarios ADD CONSTRAINT uk_usuarios_email UNIQUE (email)',
    'SELECT 1'
);
PREPARE stmt FROM @usuarios_email_unique_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_cuentas_usuario_exists := (
    SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE table_schema = DATABASE()
      AND table_name = 'cuentas_financieras'
      AND constraint_name = 'fk_cuentas_financieras_usuario'
      AND constraint_type = 'FOREIGN KEY'
);
SET @fk_cuentas_usuario_sql := IF(
    @fk_cuentas_usuario_exists = 0,
    'ALTER TABLE cuentas_financieras ADD CONSTRAINT fk_cuentas_financieras_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)',
    'SELECT 1'
);
PREPARE stmt FROM @fk_cuentas_usuario_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_cuentas_divisa_exists := (
    SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE table_schema = DATABASE()
      AND table_name = 'cuentas_financieras'
      AND constraint_name = 'fk_cuentas_financieras_divisa'
      AND constraint_type = 'FOREIGN KEY'
);
SET @fk_cuentas_divisa_sql := IF(
    @fk_cuentas_divisa_exists = 0,
    'ALTER TABLE cuentas_financieras ADD CONSTRAINT fk_cuentas_financieras_divisa FOREIGN KEY (divisa_id) REFERENCES divisas(id)',
    'SELECT 1'
);
PREPARE stmt FROM @fk_cuentas_divisa_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_rubros_usuario_exists := (
    SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE table_schema = DATABASE()
      AND table_name = 'rubros'
      AND constraint_name = 'fk_rubros_usuario'
      AND constraint_type = 'FOREIGN KEY'
);
SET @fk_rubros_usuario_sql := IF(
    @fk_rubros_usuario_exists = 0,
    'ALTER TABLE rubros ADD CONSTRAINT fk_rubros_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)',
    'SELECT 1'
);
PREPARE stmt FROM @fk_rubros_usuario_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_rubros_parent_exists := (
    SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE table_schema = DATABASE()
      AND table_name = 'rubros'
      AND constraint_name = 'fk_rubros_parent'
      AND constraint_type = 'FOREIGN KEY'
);
SET @fk_rubros_parent_sql := IF(
    @fk_rubros_parent_exists = 0,
    'ALTER TABLE rubros ADD CONSTRAINT fk_rubros_parent FOREIGN KEY (parent_id) REFERENCES rubros(id)',
    'SELECT 1'
);
PREPARE stmt FROM @fk_rubros_parent_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_presupuestos_origen_exists := (
    SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE table_schema = DATABASE()
      AND table_name = 'presupuestos'
      AND constraint_name = 'fk_presupuestos_origen'
      AND constraint_type = 'FOREIGN KEY'
);
SET @fk_presupuestos_origen_sql := IF(
    @fk_presupuestos_origen_exists = 0,
    'ALTER TABLE presupuestos ADD CONSTRAINT fk_presupuestos_origen FOREIGN KEY (presupuesto_origen_id) REFERENCES presupuestos(id)',
    'SELECT 1'
);
PREPARE stmt FROM @fk_presupuestos_origen_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_partidas_presupuesto_exists := (
    SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE table_schema = DATABASE()
      AND table_name = 'partidas_planificadas'
      AND constraint_name = 'fk_partidas_planificadas_presupuesto'
      AND constraint_type = 'FOREIGN KEY'
);
SET @fk_partidas_presupuesto_sql := IF(
    @fk_partidas_presupuesto_exists = 0,
    'ALTER TABLE partidas_planificadas ADD CONSTRAINT fk_partidas_planificadas_presupuesto FOREIGN KEY (presupuesto_id) REFERENCES presupuestos(id)',
    'SELECT 1'
);
PREPARE stmt FROM @fk_partidas_presupuesto_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_partidas_rubro_exists := (
    SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE table_schema = DATABASE()
      AND table_name = 'partidas_planificadas'
      AND constraint_name = 'fk_partidas_planificadas_rubro'
      AND constraint_type = 'FOREIGN KEY'
);
SET @fk_partidas_rubro_sql := IF(
    @fk_partidas_rubro_exists = 0,
    'ALTER TABLE partidas_planificadas ADD CONSTRAINT fk_partidas_planificadas_rubro FOREIGN KEY (rubro_id) REFERENCES rubros(id)',
    'SELECT 1'
);
PREPARE stmt FROM @fk_partidas_rubro_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_partidas_origen_exists := (
    SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE table_schema = DATABASE()
      AND table_name = 'partidas_planificadas'
      AND constraint_name = 'fk_partidas_planificadas_origen'
      AND constraint_type = 'FOREIGN KEY'
);
SET @fk_partidas_origen_sql := IF(
    @fk_partidas_origen_exists = 0,
    'ALTER TABLE partidas_planificadas ADD CONSTRAINT fk_partidas_planificadas_origen FOREIGN KEY (partida_origen_id) REFERENCES partidas_planificadas(id)',
    'SELECT 1'
);
PREPARE stmt FROM @fk_partidas_origen_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_transacciones_presupuesto_exists := (
    SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE table_schema = DATABASE()
      AND table_name = 'transacciones'
      AND constraint_name = 'fk_transacciones_presupuesto'
      AND constraint_type = 'FOREIGN KEY'
);
SET @fk_transacciones_presupuesto_sql := IF(
    @fk_transacciones_presupuesto_exists = 0,
    'ALTER TABLE transacciones ADD CONSTRAINT fk_transacciones_presupuesto FOREIGN KEY (presupuesto_id) REFERENCES presupuestos(id)',
    'SELECT 1'
);
PREPARE stmt FROM @fk_transacciones_presupuesto_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_transacciones_rubro_exists := (
    SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE table_schema = DATABASE()
      AND table_name = 'transacciones'
      AND constraint_name = 'fk_transacciones_rubro'
      AND constraint_type = 'FOREIGN KEY'
);
SET @fk_transacciones_rubro_sql := IF(
    @fk_transacciones_rubro_exists = 0,
    'ALTER TABLE transacciones ADD CONSTRAINT fk_transacciones_rubro FOREIGN KEY (rubro_id) REFERENCES rubros(id)',
    'SELECT 1'
);
PREPARE stmt FROM @fk_transacciones_rubro_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_transacciones_cuenta_exists := (
    SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE table_schema = DATABASE()
      AND table_name = 'transacciones'
      AND constraint_name = 'fk_transacciones_cuenta'
      AND constraint_type = 'FOREIGN KEY'
);
SET @fk_transacciones_cuenta_sql := IF(
    @fk_transacciones_cuenta_exists = 0,
    'ALTER TABLE transacciones ADD CONSTRAINT fk_transacciones_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuentas_financieras(id)',
    'SELECT 1'
);
PREPARE stmt FROM @fk_transacciones_cuenta_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_transacciones_partida_exists := (
    SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE table_schema = DATABASE()
      AND table_name = 'transacciones'
      AND constraint_name = 'fk_transacciones_partida_planificada'
      AND constraint_type = 'FOREIGN KEY'
);
SET @fk_transacciones_partida_sql := IF(
    @fk_transacciones_partida_exists = 0,
    'ALTER TABLE transacciones ADD CONSTRAINT fk_transacciones_partida_planificada FOREIGN KEY (partida_planificada_id) REFERENCES partidas_planificadas(id)',
    'SELECT 1'
);
PREPARE stmt FROM @fk_transacciones_partida_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
