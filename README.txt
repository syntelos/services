
Overview

    This package loads a services file containing a list of one or
    more names.  

    This list is returned to the caller as a possibly empty list using
    "services.Classes".

    "Classes" will attempt to load named classes using the Thread
    Context Class Loader.  Whether or not any classes from the list
    are loaded, the list of names read from the services file is
    maintained for the use of the caller.

    The services file is found in the class path location "META-INF"
    within jar archives, or "WEB-INF" within web applications.

    The services file is named for the caller under the "/services/"
    directory within "META-INF" or "WEB-INF".  For the caller
    ("Classes" constructor argument) "net.soft.use.Services", the
    services file will be found by "Classes" at one of the locations
        /META-INF/services/net.soft.use.Services
    or
        /WEB-INF/services/net.soft.use.Services


Application example

    A graphical user interface may load and run GUIs for configuring
    installed extension packages.

    The application copies java code archive copies trusted files into
    its class path in order to install them.  

    In this example, trusted code is written by the authors of the
    application, signed and hashed on a trusted file system.

    The application extension installation process validates code
    archive hashes and signatures before copying the extension
    (archive file) into its class path.

    The installation process then restarts the application, or adds the
    class path member to the appropriate class loader scope.

    On starting, the application calls its services class
    "net.soft.use.Services" to load its services.

    This process may employ one of a variety of application integrity
    strategies.  The selected strategy ensures that the application
    becomes available to the user so that extension management is
    completely reliable.  A failed extension must never disable the
    ability to repair the application (e.g. remove the extension).

    Class loading may be performed by "net.soft.use.Services" in a
    static initializer, as in the following example.


        package net.soft.use;

        public class Services
            extends java.lang.Object
            implements java.lang.Runnable
        {

            public final static Services Instance = new Services();


            private final services.Classes classes;


            private Services(){
                super();
                services.Classes classes = new services.Classes(this.getClass());
            }

            public void run(){
                for (java.lang.Class clas : this.classes){
                    try {
                        // (application services specification ?)

                        Servicable instance = (Servicable)clas.newInstance();

                    }
                    catch (java.lang.Exception error){

                        // (application services specification ?)
                    }
                }
            }
        }

    In this example, the method "this.getClass" is employed as a
    highly portable technique for obtaining the java.lang.Class
    argument for the "Classes" constructor.

    This example shows a static initializer calling the "Services"
    class constructor with "new Services()".

    In this approach, a public static "Init" method may be added for
    the application Main class to call when it will load services.

    Another approach has the application Main class constructing an
    instance of the example "Services" class with arguments for the
    constructors or methods of the "Servicable" classes.

    There are many interesting and effective ways to define a
    "Services" class.

    A graphical user interface employing the 'blind' initialization
    outlined in this example would require static application binding
    or integration functions for a GUI class to access the application
    scene graph.

    Another approach is to pass a handle into the GUI scene graph to
    the Services class for the use of Servicable classes.

    More generally, instances of Servicable classes may require an
    instance of an application programming interface.


Package classes

    services.Classes

        A services resource reader that intializes classes.

        Failed class loads are logged under "services.Classes".

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

