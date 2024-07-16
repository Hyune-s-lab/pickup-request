rootProject.name = "pickup-request"

include(
    "common",

    "app:pickup-request-api",

    "domain:pickup",

    "infrastructure:mysql",
)
