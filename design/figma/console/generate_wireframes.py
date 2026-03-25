from PIL import Image, ImageDraw, ImageFont
import os

screens = {
    "01_main_menu.png": """
========================================
      PROJECT MANAGER CONSOLE APP
========================================
1. Project Management
2. Task Management
3. User Management
4. Settings (Switch DB)
5. Exit

Use Arrow Keys & Tab to navigate.
Current Storage: MySQL
========================================
""",
    "02_task_management.png": """
========================================
           TASK MANAGEMENT
========================================
1. Add New Task
2. List All Tasks
3. Update Task Status
4. Back to Main Menu

Select an option: _
========================================
""",
    "03_settings.png": """
========================================
              SETTINGS
========================================
Available Storage Backends:
[ ] 1. Binary File I/O (.dat)
[ ] 2. SQLite (JDBC)
[x] 3. MySQL (Docker)

Select backend to switch: _
========================================
"""
}

# we'll use a very simple built-in font
for filename, text in screens.items():
    img = Image.new('RGB', (600, 400), color = (30, 30, 30))
    d = ImageDraw.Draw(img)
    # The default font or generic one
    try:
        fnt = ImageFont.truetype("consolas", 18)
    except:
        fnt = ImageFont.load_default()
    
    d.text((20, 20), text, fill=(0, 255, 0), font=fnt)
    img.save(filename)
    print(f"Generated {filename}")
