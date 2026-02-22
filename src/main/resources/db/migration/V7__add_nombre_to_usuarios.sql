SET @usuarios_nombre_exists := (
    SELECT COUNT(*)
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
      AND table_name = 'usuarios'
      AND column_name = 'nombre'
);

SET @usuarios_nombre_sql := IF(
    @usuarios_nombre_exists = 0,
    'ALTER TABLE usuarios ADD COLUMN nombre VARCHAR(255) NOT NULL DEFAULT '''' AFTER updated_at',
    'SELECT 1'
);

PREPARE stmt FROM @usuarios_nombre_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
