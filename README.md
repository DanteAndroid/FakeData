# FakeData

Make faking data and Composable preview easy.

## Dependency

1. Add [Jitpack](https://jitpack.io/#DanteAndroid/FakeData) in your root build.gradle

```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

**or** following if you are using `build.gradle.kts`:

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { setUrl("https://jitpack.io") } // add this line
        google()
        mavenCentral()
    }
}
```

2. Add dependency:

```
    implementation("com.github.DanteAndroid:FakeData:0.28")
    ksp("com.github.DanteAndroid:FakeData:0.28")
```

## Usage

Add `@FakeData` on your data class, like:

```
@FakeData(
    stringOption = StringOption(prefix = "pre-", mode = StringOption.StringMode.Static),
    booleanOption = BooleanOption(staticValue = true)
)
data class MyDataClass(
    val name: String,
    val age: Int,
    val test: Boolean,
    val height: Long,
    val job: String,
    val time: LocalDate
){
    companion object{
        fun test(){
            // After successful build 
            // You can call MyDataClassFake.xxx to get fake data now!
        }
    }
}
```

Or add `@FakeData` to @Composable, like:

```
@FakeData
@Composable
fun MyComposableFun(title: String, number: Int, date: LocalDate) {
    Box(modifier = Modifier.size(200.dp, 100.dp), contentAlignment = Alignment.Center) {
        Text(text = title + number + date)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 360)
@Preview(showBackground = true)
@Composable
fun MyComposableFunPreview() {
		// You can call MyComposableFake() in Preview to get fake preview now!
    MyComposableFunFake()
}
```



