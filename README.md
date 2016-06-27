# OrderBook
Программа считывает, исполняет и сортирует данные о торговых приказах по биржевым инструментам.</br>
Данные считываются из XML файла. Файл с исходными данными может содержать приказы на куплю/продажу инструмента и приказы на удаление, ранее введенного торгового приказа.</br>
Формат приказа:</br>
1. Тип приказа - AddOrder(добавить торговый приказ) и DeleteOrder (удалить ранее введеный приказ).</br>
2. Торговая книга - "book" - для каждого инструмента.</br>
3. Тип операции - "operation" - SELL(приказ на продажу) и BUY(приказ на покупку)</br>
4. Цена приказа - "price" - цена по которой должен быть исполнен торговый приказ.</br>
5. Объем приказа - "volume" - количество лотов для исполнения (покупки/продажи инструмента).</br>
6. Номер приказа - "orderId" - номер торгового приказа. А для приказа на удаление - номер приказа который необходимо отменить (удалить).</br>

Программа состоит из следующих типов:</br>
1. Класс ReadConnectorXML - считывает данные из XML файла, создает объекты типа Order  и возвращает их DataReader'у.</br>
2. Класс SAXHandler - обработчик для парсинга XML файла.</br>
3. Класс ReadConnectorSXP - считывает данные из XML файла (используя SimpleParser), создает объекты типа Order  и возвращает их DataReader'у.</br>
4. Класс SimpleParser - самописный парсер XML файла.Для парсинга использует String.split с решулярными выражениями.</br>
5. Класс SimpleParser2 - самописный парсер. Для парсинга рекурсивно обрабатывает строку на наличие паттернов методом indexOf.</br>
6. Класс DataReader - считывает данные из источника, посредством объекта реализующего интерфейс ReadConnector.</br>
7. Класс DeleteThread - удаляет приказы список приказов на удаление формируется DataReader'ом.</br>
8. Класс DeleteMarker - содержит список приказов на удаление.</br> 
9. Класс StorageLHMDel - осуществляет хранение торговых приказов. Их добавление, удаление и предоставляет итератор.</br> 
10. Класс Order - представление торгового приказа. Приказ на удаление, также представляется объектом данного тип приказа DeleteOrder.</br>
11. Класс OrderBookProcessor - считывает из хранилища приказы, и если относятся к данной книге, исполняет их встречными приказами или помещает их в спиосок приказов на покупку и продажу.</br>
12. Класс Result - результат обработки классом OrderBookProcessor торговых приказов. Хранит книгу, а также списки приказов на покупку/продажу.</br>
13. Класс PrintResults - производит уплотнение(неисполненные и неудаленные приказы одного типа при совпадении цены отображаются одной строкой с суммированием объемов) и отображает приказы на покупку в консоли.</br>
14. Класс AscendingPrice - компаратор для сортировки объектов Order по значению цены в восходящем порядке.</br> 
15. Класс DescendingPrice - компаратор для сортировки объектов Order по значению цены в нисходящем порядке.</br>
