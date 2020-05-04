### Configurator lets you edit configuration files of any plugin with a GUI inside your server. It features a file browser to browse configuration files in plugin folders, and a sort of configuration browser that lets you browse nodes in a file.

Configurator currently only supports YAML files.

## Commands
* **/configurator** or **/cfg** - _Opens the GUI_

## Permissions
* **configurator.use** - _Access to the GUI, i.e edit configs_
* **configurator.reload** - _Access to **/cfg reload**_

## Customization
### Customizing messages
> All messages that are visible to the user (excluding console, admin commands like reload...) are customizable. This includes text inside the GUI.

> **_IMPORTANT:_** Do **_NOT_** edit values in _lang_default.properties_, they will be reset on plugin load.

> To use special characters, you may have to use escaped Unicode characters, such as _\u00BB_ for **»**.
A list of available Unicode characters [is available here](http://www.fileformat.info/info/unicode/char/a.htm)

1. Copy the contents from _lang_default.properties_ to a new file, _lang\_\<code>.properties_, where \<code> is any one word.
2. Change the values however you like, and save.
3. Change _lang_ in the config.yml file from "default" to whatever code you chose for your language file.
4.  Start the server or reload using **/cfg reload**.
5. In the future, the plugin might warn you that your language file is missing values, and that the plugin will fall back to the default values of those that are missing. **To fix this**, copy _(only the missing ones, listed in console)_ values from _lang_default.properties_ to your file, edit them and reload. Order doesn't matter, so you can paste them on the last line.

## Other info
> **_IMPORTANT:_** This plugin **_WILL_** mess up comments in config files! This will hopefully be fixed soon.
