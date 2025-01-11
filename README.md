# پیمایشگر گراف

در این پروژه، یک پیمایشگر ساده گراف با دو روش اول-سطح و اول-عمق پیاده سازی شده است.

## گام اول

در گام اول برای حذف وابستگی پروژه به کتابخانه لازم است یک interface مشترک برای گراف ایجاد کنیم تا به طور کلی، عملکرد‌های مورد نیاز برای ساخت گرافمان را تعریف کند.
برای این منظور، واسط GraphAdapter را به صورت زیر اضافه می‌کنیم.
توجه شود که با توجه به اینکه در صورت سوال بیان شده است که راس‌های گراف اعداد صحیح و یال‌های آن رشته هستند، این واسط را به صورت Generic تعریف نمی‌کنیم، اما در صورت عدم بیان این موضوع در یک حالت کلی‌تر میتوان چنین نیز کرد.

```java
public interface GraphAdapter {

    void addVertex(Integer vertex);
    void addEdge(String edge, Integer starVertex, Integer endVertex);
    Collection<Integer> getNeighbors(Integer vertex);
}
```

## گام دوم

در ادامه لازم است پیاده‌سازی‌ واسط تعریف شده را برای کتابخانه اولیه‌مان، یعنی Jung، انجام دهیم. در نتیجه خواهیم داشت:

```java
public class JungGraphAdapter implements GraphAdapter {
    private final SparseMultigraph<Integer, String> graph;

    public JungGraphAdapter() {
        this.graph = new SparseMultigraph<>();
    }

    @Override
    public void addVertex(Integer vertex) {
        graph.addVertex(vertex);
    }

    @Override
    public void addEdge(String edge, Integer vertex1, Integer vertex2) {
        graph.addEdge(edge, vertex1, vertex2);
    }

    @Override
    public Collection<Integer> getNeighbors(Integer vertex) {
        return graph.getNeighbors(vertex);
    }
}
```

که این پیاده‌سازی صرفا همان پیاده‌سازی گدشته است اما با این تفاوت که این بار آن را به عنوان یک نمونه پیاده‌سازی از واسط GraphAdapter ایجاد کرده‌ایم.

## گام سوم

حال لازم است بخش‌های مختلف پروژه را به‌گونه‌ای تغییر دهیم تا به جای استفاده مستقیم از کلاس concrete مربوط به کتابخانه Jung، از این واسط استفاده کنند. در واقع در این مرحله بایستی اصل DIP را برقرار کنیم تا جهت وابستگی اجزای سیستم به یکدیگر را وارونه کنیم.

در اثر این تغییر، هر سه کلاس `Main`، `BfsTraverser` و `DfsTraverser` دچار تغییرات بسیار جزیی شدند.

## گام چهارم

در این مرحله کافیست برای کتابخانه جدید نیز مشابه کاری که برای کتابخانه پیشین کردیم، نمونه پیاده‌سازی‌ای از واسط `GraphAdapter` ارائه کنیم. در نتیجه خواهیم داشت:

```java
public class JGraphAdapter implements GraphAdapter {
    private final DefaultDirectedGraph<Integer, String> graph;

    public JGraphAdapter() {
        this.graph = new DefaultDirectedGraph<>(String.class);
    }

    @Override
    public void addVertex(Integer vertex) {
        graph.addVertex(vertex);
    }

    @Override
    public void addEdge(String edge, Integer vertex1, Integer vertex2) {
        graph.addEdge(vertex1, vertex2);
    }

    @Override
    public Collection<Integer> getNeighbors(Integer vertex) {
        return graph.outgoingEdgesOf(vertex).stream()
                .map(graph::getEdgeTarget)
                .collect(Collectors.toSet());
    }
}
```

توجه: این کلاس بر اساس مستندات کتابخانه نوشته‌ شده است.

---

## تحلیل وجود الگوی Strategy

در این پیاده‌سازی از الگوی Strategy به‌طور مؤثری استفاده شده است. الگوی Strategy این امکان را فراهم می‌کند که رفتار traverse به راحتی برای هر یک از پیاده‌سازی‌های BFS و DFS تغییر یابد. هر یک از این الگوریتم‌ها با پیاده‌سازی‌های خاص خود از واسط Traverser طراحی شده‌اند. این واسط، متد traverse(Integer startVertex) را در اختیار می‌گذارد که بسته به پیاده‌سازی، رفتار گراف را برای پیمایش گراف تنظیم می‌کند. استفاده از این الگو به ما این امکان را می‌دهد که بدون تغییر در کدهای اصلی، نوع پیمایش گراف را به‌سادگی از BFS به DFS تغییر دهیم.

```java
public interface Traverser {
    List<Integer> traverse(Integer startVertex);
}
```

این رویکرد انعطاف‌پذیری بالایی را در کد به وجود می‌آورد، چرا که تغییرات در الگوریتم‌های پیمایش بدون تأثیر بر سایر بخش‌های سیستم امکان‌پذیر است. علاوه بر این، این الگو به‌خوبی با اصول SOLID، به‌ویژه اصل باز/بسته (Open/Closed Principle) تطابق دارد. این ویژگی به توسعه‌دهندگان این امکان را می‌دهد که بتوانند به راحتی ویژگی‌های جدیدی به سیستم اضافه کنند بدون آنکه بر کدهای موجود تأثیر بگذارند.

### استفاده از Adapter (object scope)

در این پروژه، از الگوی Adapter به‌صورت object scope استفاده کرده‌ایم. این انتخاب به دلیل سادگی بیشتر آن بوده است. به‌جای استفاده از وراثت که در صورت تغییرات در کلاس‌های پایه می‌تواند مشکلاتی مانند افزایش coupling را به همراه داشته باشد، از composition بهره برده‌ایم. با این کار، ما به‌جای ارث‌بری مستقیم از کلاس‌ها، نمونه‌هایی از گراف‌ها را در داخل کلاس‌های Adapter نگه می‌داریم. این روش موجب می‌شود که سیستم از تغییرات وابسته به کتابخانه‌ها ایمن بماند.

مزیت دیگر این انتخاب آن است که توسعه و نگهداری سیستم ساده‌تر خواهد بود. استفاده از رابطه composition به‌جای وراثت از اصول طراحی شی‌گرای پایدارتر است و باعث کاهش پیچیدگی‌ها و انعطاف‌پذیری بیشتر سیستم می‌شود.

### روند پیاده‌سازی این الگو

پیاده‌سازی الگوی Adapter در این پروژه به‌سادگی از یک واسط برای گراف آغاز شد. این واسط، عملکردهای مورد نیاز برای گراف را تعریف کرد، از جمله افزودن رأس، افزودن یال، و دریافت همسایگان. سپس، با استفاده از کتابخانه Jung، کلاس JungGraphAdapter پیاده‌سازی شد تا این واسط را در خود جای دهد و تمام رفتارهای مورد نیاز برای تعامل با گراف را فراهم کند.

در مرحله بعدی، مشابه همین کار برای کتابخانه JGraphT انجام شد و کلاس JGraphAdapter پیاده‌سازی شد تا عملکردهای مشابهی را برای گراف‌های این کتابخانه فراهم کند.

نکته مهم این است که پیش از این تغییرات، پروژه به‌طور مستقیم به کتابخانه Jung وابسته بود. با استفاده از الگوی Adapter و اصل Dependency Inversion Principle (DIP)، وابستگی‌ها از بین رفتند و به سادگی با تغییر در شیء گراف تزریقی، کتابخانه به JGraphT تغییر یافت. این تغییرات به حداقل تأثیرگذاری بر کدهای اصلی پروژه منجر شد و انعطاف‌پذیری پروژه را بهبود بخشید.

### تغییر کتابخانه

با اعمال تغییرات مربوط به الگوی Adapter، تغییر کتابخانه از Jung به JGraphT به‌راحتی و تنها با تغییر در شیء گراف تزریقی در کلاس Main انجام شد. بعد از این تغییرات، فقط نیاز به افزودن پیاده‌سازی جدید از واسط GraphAdapter و تغییر شیء تزریقی در کد داشتیم. کدهای کلاس‌های اصلی، مانند BfsTraverser و DfsTraverser، بدون تغییر باقی ماندند.

---
