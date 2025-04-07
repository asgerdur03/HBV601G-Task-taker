## Lokaverkeni í HBV601G
- Ásgerður Júlía
- Freydís Xuan
- Hermann Ingi
- Vilborg

Task manager application
# Task Taker Android app

Final project in HBV601G - Software project 2

## TODO 
Routes to implements, by priority:

- DELETE tasks
- PATCH allt
- PUT allt

### TODO user stories, og hvernig uppfyllum við það:

- [ ] Edit task: nota `PATCH /tasks/{id}`, autofill fields sem er ekki breytt. 
Uppfyllir einnig *Track progress* og *Set Reminder*. 
- [ ] Upload profile picture: `POST /upload-pic` í settings, mobile specific, birta mynd í horni?
- [ ] Offline accessibility: Get tasks, drita í sqlite database, (ef ekkert net, fetcha þaðan)
- [ ] Pomodoro: nope (ef við höfum tíma)
- [ ] Archive/Favorites: OnButtonclick, kalla á viðeigandi route, reload. 
- [ ] Filter tasks: Svipað, onButtonClick, kalla á api með filter skilyrðum



