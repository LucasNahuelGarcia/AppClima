# Aplicacion de Clima y Astronomía

## LLM Workflow
Antes de cualquier sesión con IA, incluir `llm_instructions.md` como contexto.
Paso 0 obligatorio en sesión nueva: explorar `Ref_src_movies/` con el prompt del archivo.

## Configurar NASA API key (local.properties)

Coloca tu clave de NASA APOD en el archivo `local.properties` en la raíz del proyecto.
Este repositorio ya contiene `local.properties` con el formato correcto.

1. Abrí `local.properties` (raíz del repo).
2. Editá la línea `nasa.api.key=` y poné tu clave: `nasa.api.key=TU_API_KEY`
3. No commitees `local.properties`. El archivo está incluido en `.gitignore`.

La aplicación leerá esta configuración si no existe la variable de entorno `NASA_API_KEY`.
