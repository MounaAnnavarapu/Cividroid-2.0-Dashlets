# Cividroid-2.0-Dashlets
====================


Dashlets is destined to merge with [Cividroid](https://github.com/Jagatheesan/CiviDroid---Alpha) as Cividroid 2.0. Here is a brief on Dashlets as an independent App:

The App revolves around the concept of dashlets, providing users all the [CiviReports](https://civicrm.org/features/reports) by syncing functionality which can further be used to create charts displaying the data visually. The CiviReports data can be saved for offline references. The charts can be used to create widgets for the home screens.

----------
Features:
-------------

- The App needs only a apikey, sitekey and url of your website. No login is required to use the App. 

- All the CiviReports (even a custom one, or provided by 3rd party extensions) are synced and are available as a Dashlet.

- A Dashlet tabulates the data on a page which is scrollable both vertically as well as horizontally. Each page can display upto 25 records. A pair of floating navigational arrow buttons can be used to load more data. 

- Each page of a dashlet can be saved for offline viewing. Switching between online and offline mode is seamless with a button in the toolbar.

- For every pair of valid columns (with numeric data), a corresponding chart can be generated. App supports the following charts:
> - Bar Chart
> - Horizontal Chart
> - Pie Chart
> - Cubic Line Chart

- Each chart generated can be saved to stick as a widget on homescreen.

---------
Screens
----------

<img src="https://cloud.githubusercontent.com/assets/18498028/17832087/5c8251e0-6718-11e6-9bff-5886b7cec254.png" align="right" width="281"/> </img>
The screenshot on the right is the main screen of the app where the user can select the Dashlets from the menu. As development progresses, users will be able to use this screen as a Dashboard to pin Dashlets here. It would be particularly useful on tablets.
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>

<img src="https://cloud.githubusercontent.com/assets/18498028/17832037/8a1dbff0-6717-11e6-919a-b9de2e29127c.png" align="left" width="281"/> </img>

All the CiviReports (even a custom one, or provided by 3rd party extensions) will be available as a Dashlet. The user can choose from the menu shown in the screenshot on the left.
When a dashlet in the menu is selected the details of it are displayed.
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<img src="https://cloud.githubusercontent.com/assets/18498028/17832070/1c663824-6718-11e6-8d84-d6773f4a1536.png" align="right" width="281"/> </img>
The Dashlet “Contribution Summary” is selected and by default the data is rendered in a tabular form. The data is scrollable both horizontally as well as vertically. Pagination will be provided for large set of data.
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<img src="https://cloud.githubusercontent.com/assets/18498028/17832071/1c68a0be-6718-11e6-8955-a18ecbae3a7b.png" align="left" width="281"/> </img>Clicking the floating ‘+’ button will let a user to generate a chart with desired fields.
Y-Axis must be a column with numeric values and the App will let the user select only valid columns, while X-Axis could be any column.Dashlets makes use of [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) library to render the charts.
Currently the App supports the following charts:<br/>
<br/>
Bar Chart<br/>
Horizontal Chart<br/>
Pie Chart<br/>
Cubic Line Chart

More charts and some advanced functions on the charts will be available as the development progresses.
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
---------
Charts
-------------

<img src="https://cloud.githubusercontent.com/assets/18498028/17832068/1c652a92-6718-11e6-934a-5adeddad7ba1.png" align="left" width="281"/> </img>

<img src="https://cloud.githubusercontent.com/assets/18498028/17832072/1c693326-6718-11e6-8ee1-7053d0780faf.png" align="right" width="281"/> </img>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<img src="https://cloud.githubusercontent.com/assets/18498028/17832066/1c3545e8-6718-11e6-8e86-fc5f4363e096.png" align="left" width="281"/> </img>
<img src="https://cloud.githubusercontent.com/assets/18498028/17832063/1c3247b2-6718-11e6-8236-e14f84b6390f.png" align="right" width="281"/> </img>
<br />
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br />
<br/>
<br/>
<br/>
<br/>
These charts can be generated for any Dashlet as long as they have eligible columns (columns with numeric value.)
<br/>
---------------
Offline Mode
---------------
<img src="https://cloud.githubusercontent.com/assets/18498028/17832074/1c962282-6718-11e6-8b30-13a136f0ae42.png" align="right" width="281" /> </img>
The icon on the top right is for saving the existing page offline. As soon as the user clicks the Save Offline button the toast message "Page saved" appears.
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<img src="https://cloud.githubusercontent.com/assets/18498028/17832075/1c975968-6718-11e6-9d5d-acb41590bff8.png" align="left" width="281"/> </img>
The user can see the saved pages by clicking on the "View Saved Pages" in the options menu.
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<img src="https://cloud.githubusercontent.com/assets/18498028/17832073/1c8ce0f0-6718-11e6-9887-b9a370fbc414.png" align="right" width="281"/> </img>
When a saved page is being viewed, the app goes offline. User can click on the globe icon to go online.
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
----------
Save Widgets
-----------
<img src="https://cloud.githubusercontent.com/assets/18498028/17832067/1c5b2efc-6718-11e6-81d8-e57aaeef843c.png" align="left" width="281"/> </img>
A chart can be saved for using as a widget by clicking "Save for widget" in the options.
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<img src="https://cloud.githubusercontent.com/assets/18498028/17832062/1c3256a8-6718-11e6-9fb9-344326fbdb4d.png" align="right" width="281"/> </img>
To save a chart for using as a widget the user has to name it.
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
---------
Setting up Widget on Home Screen
--------

<img src="https://cloud.githubusercontent.com/assets/18498028/17832069/1c65c2e0-6718-11e6-9ee1-e917688c30e7.png" align="left" width="281"/> </img>
The user should go to the widget setup menu, and select the Dashlets widget.
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<img src="https://cloud.githubusercontent.com/assets/18498028/17832065/1c34cc4e-6718-11e6-94fa-2f68ae0a4b56.png" align="right" width="281"/> </img>
The list of charts saved by the user to be used as widgets are displayed, for choosing the desired chart.
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
--------------------

<img src="https://cloud.githubusercontent.com/assets/18498028/17832064/1c3352e2-6718-11e6-80a1-56179d0b5b6a.png" align="right" width="281"/> </img>

<img src="https://cloud.githubusercontent.com/assets/18498028/17832061/1c29feb8-6718-11e6-9905-30be8885de00.png" align="left" width="281"/> </img>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>


Widgets displayed on Home screens

----------
Set Up Requirements
-------------
When the app is opened for the first time the user has to enter these details:

- Website's REST.php location
- Your API key
- Website's Key
