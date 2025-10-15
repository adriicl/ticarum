-- Competición inicial
INSERT INTO competition (id, name, sport, start_date, end_date, pistas) VALUES (1, 'Liga UMU', 'Baloncesto', DATE '2025-05-20', DATE '2025-06-30', 3);

-- Días reservados (element collection)
INSERT INTO competition_reserved_days (competition_id, reserved_day) VALUES (1, DATE '2025-05-20');
INSERT INTO competition_reserved_days (competition_id, reserved_day) VALUES (1, DATE '2025-05-21');
INSERT INTO competition_reserved_days (competition_id, reserved_day) VALUES (1, DATE '2025-05-22');

-- Equipos iniciales
INSERT INTO team (id, name, competition_id) VALUES (1, 'Equipo A', 1);
INSERT INTO team (id, name, competition_id) VALUES (2, 'Equipo B', 1);
INSERT INTO team (id, name, competition_id) VALUES (3, 'Equipo C', 1);
INSERT INTO team (id, name, competition_id) VALUES (4, 'Equipo D', 1);
INSERT INTO team (id, name, competition_id) VALUES (5, 'Equipo E', 1);
INSERT INTO team (id, name, competition_id) VALUES (6, 'Equipo F', 1);

