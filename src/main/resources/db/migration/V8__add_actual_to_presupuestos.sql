ALTER TABLE presupuestos
    ADD COLUMN actual BOOLEAN NOT NULL DEFAULT FALSE;

UPDATE presupuestos
SET actual = FALSE
WHERE actual IS NULL;
