
    services.Classes

        A services resource reader that intializes classes

    services.Names

        A services resource reader that reads class names

    Resource

        A service resource resides at the class path source location

           /META-INF/services/class.Name 

        or

           /WEB-INF/services/class.Name . 

    Format

        A service resource lists class names in (the class loader) dot
        notation (of Class.forName), one per line.

