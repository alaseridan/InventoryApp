# InventoryApp
A custom made inventory app with a shopping list for android

This Android app was made to the specific needs of a small restaurant business. It has two major features: An inventory list and a shopping list

The inventory list is easily editable by the user to keep an accurate count of their inventory. When an item's stock count falls below a defined minimum level that item is added to the shopping list. In the shopping list the user is told the quantity of that item they need to replace in order to keep the stock at the preferred level. The user may also manually add items to the shopping cart.

The shopping list displays the items and quantities that need to be restocked. When the user picks up an item they are prompted to tell the system how much they are picking up. Because of a prompt's nature of covering part of the screen the prompt displays the item's name, unit of measure, and reminds the user how much of it they should pick up. Once the amount is confirmed the system adds that quantity to the inventory and removes that amount from the shopping list, if the full amount wasn't picked up the item stays in the shopping list.

This app keeps a time-stamped record of all changes made to both the inventory and the shopping list. The history list, inventory list, and shopping list can be exported to an XML file. In the future I plan to add the options to export to plain text and CSV files.

This project uses the appcompat_v7 library for its themes.
