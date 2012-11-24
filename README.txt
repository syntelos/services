
    services.Services

        A services resource reader that intializes classes

    Resource

        A service resource resides at the class path source location

           /META-INF/services/class.Name 

        or

           /WEB-INF/services/class.Name . 

    Format

        A service resource lists class names in (the class loader) dot
        notation (of Class.forName), one per line.

