# Aplicacion de Clima y Astronomía

## LLM Workflow
Antes de cualquier sesión con IA, incluir `llm_instructions.md` como contexto.
Paso 0 obligatorio en sesión nueva: explorar `Ref_src_movies/` con el prompt del archivo.

## Configuracion de NASA APOD
Antes de ejecutar la app de desktop, definir `NASA_API_KEY` en el entorno o en `local.properties` en la raiz del proyecto:

```properties
NASA_API_KEY=tu_api_key
```

La app toma Bahía Blanca como coordenada inicial por defecto.