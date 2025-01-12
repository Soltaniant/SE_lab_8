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


### روند پیاده‌سازی این الگو

پیاده‌سازی الگوی Adapter در این پروژه به‌سادگی از یک واسط برای گراف آغاز شد. این واسط، عملکردهای مورد نیاز برای گراف را تعریف کرد، از جمله افزودن رأس، افزودن یال، و دریافت همسایگان. سپس، با استفاده از کتابخانه Jung، کلاس JungGraphAdapter پیاده‌سازی شد تا این واسط را در خود جای دهد و تمام رفتارهای مورد نیاز برای تعامل با گراف را فراهم کند.

در مرحله بعدی، مشابه همین کار برای کتابخانه JGraphT انجام شد و کلاس JGraphAdapter پیاده‌سازی شد تا عملکردهای مشابهی را برای گراف‌های این کتابخانه فراهم کند.

نکته مهم این است که پیش از این تغییرات، پروژه به‌طور مستقیم به کتابخانه Jung وابسته بود. با استفاده از الگوی Adapter و اصل Dependency Inversion Principle (DIP)، وابستگی‌ها از بین رفتند و به سادگی با تغییر در شیء گراف تزریقی، کتابخانه به JGraphT تغییر یافت. این تغییرات به حداقل تأثیرگذاری بر کدهای اصلی پروژه منجر شد و انعطاف‌پذیری پروژه را بهبود بخشید.

### تغییر کتابخانه

با اعمال تغییرات مربوط به الگوی Adapter، تغییر کتابخانه از Jung به JGraphT به‌راحتی و تنها با تغییر در شیء گراف تزریقی در کلاس Main انجام شد. بعد از این تغییرات، فقط نیاز به افزودن پیاده‌سازی جدید از واسط GraphAdapter و تغییر شیء تزریقی در کد داشتیم. کدهای کلاس‌های اصلی، مانند BfsTraverser و DfsTraverser، بدون تغییر باقی ماندند.

---
