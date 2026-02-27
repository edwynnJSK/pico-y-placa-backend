# language: es
@circulacion
Característica: Validación de circulación vehicular Pico y Placa

  Como ciudadano del Distrito Metropolitano de Quito
  Quiero consultar si mi vehículo tiene restricción de circulación
  Para evitar multas y cumplir con la Resolución establecida.

  Escenario: Escenario 1 — Vehículo restringido en franja de mañana
    Dado que la placa es "ABC-1234"
    Y la fecha y hora es un "martes" a las "07:00"
    Cuando se consulta la restricción
    Entonces el resultado es RESTRINGIDO
    Y el motivo es "RESTRICCION_ACTIVA"

  Escenario: Escenario 2 — Mismo vehículo fuera de franja
    Dado que la placa es "ABC-1234"
    Y la fecha y hora es un "martes" a las "12:00"
    Cuando se consulta la restricción
    Entonces el resultado es LIBRE
    Y el motivo es "FUERA_FRANJA"

  Escenario: Escenario 3 — Día feriado
    Dado que la placa es "ABC-1234"
    Y la fecha y hora es un "feriado nacional" a las "08:00"
    Cuando se consulta la restricción
    Entonces el resultado es LIBRE
    Y el motivo es "FERIADO"

  Escenario: Escenario 4 — Fin de semana
    Dado que la placa es "ZZZ-9999"
    Y la fecha y hora es un "sábado" a las "08:00"
    Cuando se consulta la restricción
    Entonces el resultado es LIBRE
    Y el motivo es "FIN_SEMANA"

  Escenario: Escenario 5 — Placa con dígito que NO aplica el día
    Dado que la placa es "ABC-1231"
    Y la fecha y hora es un "martes" a las "07:00"
    Cuando se consulta la restricción
    Entonces el resultado es LIBRE
    Y el motivo es "DIGITO_NO_APLICA"

  Escenario: Escenario 6 — Placa inválida
    Dado que la placa es "AB-12"
    Cuando se consulta la restricción
    Entonces el sistema retorna error "PLACA_INVALIDA"

  Escenario: Escenario 7 — Fecha anterior al momento actual
    Dado que la fecha y hora es anterior a ahora
    Cuando se consulta la restricción
    Entonces el sistema retorna error "FECHA_ANTERIOR"

  Escenario: Escenario 8 — Borde exacto de franja — 09:30 es restringido
    Dado que la placa es "ABC-1234"
    Y la fecha y hora es un "martes" a las "09:30"
    Cuando se consulta la restricción
    Entonces el resultado es RESTRINGIDO

  Escenario: Escenario 9 — Borde exacto de franja — 09:31 es libre
    Dado que la placa es "ABC-1234"
    Y la fecha y hora es un "martes" a las "09:31"
    Cuando se consulta la restricción
    Entonces el resultado es LIBRE
    Y el motivo es "FUERA_FRANJA"

  Escenario: Escenario 10 — Vehículo comercial con advertencia de exención
    Dado que la placa es "AAA-1234"
    Y la fecha y hora es un "martes" a las "07:00"
    Cuando se consulta la restricción
    Entonces tieneAdvertenciaExencion es verdadero
