-- V2 — Datos semilla: Reglas Pico y Placa 
INSERT INTO reglas_restriccion (dia_semana, digitos, franja_inicio_1, franja_fin_1, franja_inicio_2, franja_fin_2, vigente_desde, activo)
VALUES 
('MONDAY',    '1,2', '06:00:00', '09:30:00', '16:00:00', '20:00:00', '2021-12-15', TRUE),
('TUESDAY',   '3,4', '06:00:00', '09:30:00', '16:00:00', '20:00:00', '2021-12-15', TRUE),
('WEDNESDAY', '5,6', '06:00:00', '09:30:00', '16:00:00', '20:00:00', '2021-12-15', TRUE),
('THURSDAY',  '7,8', '06:00:00', '09:30:00', '16:00:00', '20:00:00', '2021-12-15', TRUE),
('FRIDAY',    '9,0', '06:00:00', '09:30:00', '16:00:00', '20:00:00', '2021-12-15', TRUE);
