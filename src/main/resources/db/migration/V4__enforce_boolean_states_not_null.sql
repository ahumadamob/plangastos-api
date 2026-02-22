UPDATE presupuestos
SET inactivo = FALSE
WHERE inactivo IS NULL;

ALTER TABLE presupuestos
    MODIFY COLUMN inactivo BOOLEAN NOT NULL;

UPDATE partidas_planificadas
SET consolidado = FALSE
WHERE consolidado IS NULL;

ALTER TABLE partidas_planificadas
    MODIFY COLUMN consolidado BOOLEAN NOT NULL;

UPDATE rubros
SET activo = FALSE
WHERE activo IS NULL;

ALTER TABLE rubros
    MODIFY COLUMN activo BOOLEAN NOT NULL;

UPDATE cuentas_financieras
SET activo = FALSE
WHERE activo IS NULL;

ALTER TABLE cuentas_financieras
    MODIFY COLUMN activo BOOLEAN NOT NULL;
