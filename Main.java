package StreamAPI.ThirdTask;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Stream;

/*
Intel(R) Core(TM) i5-3570 CPU@ 3.40GHz, 3401 МГц ядер4 логических процессоров: 4
 */
public class Main {
    /*
        479286
        49.51072968529869
        5217485
        Process time: 6.0767448 s
        без parallelStream, но я включил и процесс инициализации людей
            480149
            49.523984588732674
            5224325
            Process time: 4.7972259 s
            c parallelStream, но я включил и процесс инициализации людей
         */
        /*
        тесты с колличеством без parallelStream | c parallelStream
        100:
        6 | 5
        55.5 | 47.48979591836735
        40 | 35
        Process time: 0.0981208 s | 0.0820746 s
            1000:
            38 | 48
            49.15810276679842 | 48.23353293413174
            424 | 432
            Process time: 0.0737297 s | 0.098237 s
                10_000:
                414 | 393
                49.535735735735734 | 49.134361680535825
                4296 | 4401
                Process time: 0.0935188 s | 0.0974833 s
                    100_000:
                    3981 | 4050
                    49.54123198216134 | 49.41772787530631
                    43765 | 43610
                    Process time: 0.1251524 s | 0.15664 s
                        1_000_000:
                        40124 | 40183
                        49.451940868785094 | 49.500104862629954
                        435723 | 434974
                        Process time: 0.3573536 s | 0.3540183 s
                            10_000_000:
                            398511 | 399452
                            49.52566813451409 | 49.51238656841147
                            4349774 | 4351580
                            Process time: 4.6934391 s | 4.3223278 s
         */
    public static void main(String[] args) {
        long[] optionSIZE = {100, 1_000, 10_000, 100_000, 1_000_000, 10_000_000};
        List<Double> list = new ArrayList<>();
        for (long SIZE : optionSIZE) {
            List<People> peoples = initialPeople(SIZE);
            System.out.println(SIZE + ":");
            list.add(testSpeed(peoples));
            System.out.println("-------------");
        }
        System.out.println(list);
        //[0.0837352, 0.0022352, 0.0029553, 0.0319169, 0.0539706, 0.4839111] stream
        //[0.0663803, 0.0015533, 0.003758, 0.0098427, 0.0179315, 0.1645438] parallelStream
        List<Double> listWithOutParallelStream = Arrays.asList(0.0837352, 0.0022352, 0.0029553, 0.0319169, 0.0539706, 0.4839111);
        List<Double> listWithParallelStream = Arrays.asList(0.0663803, 0.0015533, 0.003758, 0.0098427, 0.0179315, 0.1645438);
        for (int a = 0; a < listWithOutParallelStream.size() - 1; a++) {
            System.out.println(((listWithOutParallelStream.get(a) - listWithParallelStream.get(a))
                    * 100) / listWithOutParallelStream.get(a));
        }
    }

    static List<People> initialPeople(long SIZE) {
        List<String> names = Arrays.asList("Иванов", "Петров", "Сидоров");
        List<People> peoples = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            peoples.add(new People(names.get(
                    new Random().nextInt(names.size())),
                    new Random().nextInt(100),
                    Sex.randomSex()));
        }
        return peoples;
    }

    static void militaryMen(List<People> peoples) {
        Stream<People> stream = peoples.parallelStream();
        long list = stream.filter(s -> s.getSex().equals(Sex.MAN))
                .filter(s -> s.getAge() > 18 && s.getAge() < 27)
                .count();
        System.out.println(list);
    }

    static void averageManAge(List<People> peoples) {
        OptionalDouble averageMan = peoples.parallelStream().filter(s -> s.getSex().equals(Sex.MAN))
                .mapToInt(People::getAge)
                .average();
        if (averageMan.isPresent()) {
            System.out.println(averageMan.getAsDouble());
        }
    }

    static void potentiallyWorkingPeople(List<People> peoples) {
        Stream<People> stream3 = peoples.parallelStream();
        Long potentiallyWorkingPeople = stream3.filter(s -> s.getSex().equals(Sex.MAN) ?
                s.getAge() > 18 && s.getAge() < 65 :
                s.getAge() > 18 && s.getAge() < 60)
                .count();
        System.out.println(potentiallyWorkingPeople);
    }

    static double testSpeed(List<People> peoples) {
        long startTime = System.nanoTime();
        militaryMen(peoples);
        averageManAge(peoples);
        potentiallyWorkingPeople(peoples);
        long stopTime = System.nanoTime();
        double processTime = (double) (stopTime - startTime) / 1_000_000_000.0;
        System.out.println("Process time: " + processTime + " s");
        return processTime;
    }
}
