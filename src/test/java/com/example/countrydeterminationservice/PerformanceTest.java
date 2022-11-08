package com.example.countrydeterminationservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * JMH provides its own default paradigm of writing benchmarks by creating fat jars
 * which are than run and execute all the benchmarks included.
 * This approach proves to be no good fit when it comes to developer experience within a typical Spring app.
 * Developers are used to write tests and Spring provides a ton of support for this.
 * So the idea is to reuse Springs test infrastructure for benchmarks.
 * <a href="https://gist.github.com/msievers/ce80d343fc15c44bea6cbb741dde7e45">guide</a>
 */
@SpringBootTest
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class PerformanceTest {

    @Test
    @EnabledIfSystemProperty(named = "spring.profiles.active", matches = "benchmark")
    public void executeJmhRunner() throws RunnerException {
        Options opt = new OptionsBuilder()
                // set the class name regex for benchmarks to search for to the current class
                .include("\\." + this.getClass().getSimpleName() + "\\.")
                .warmupIterations(3)
                .measurementIterations(3)
                // do not use forking or the benchmark methods will not see references stored within its class
                .forks(0)
                // do not use multiple threads
                .threads(1)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .resultFormat(ResultFormatType.JSON)
                //.result("/dev/null") // set this to a valid filename if you want reports
                .shouldFailOnError(true)
                .jvmArgs("-server")
                .build();

        new Runner(opt).run();
    }


    private static CountryService countryService;

    @Autowired
    void setCountryService(CountryService countryService) {
        PerformanceTest.countryService = countryService;
    }

    /**
     * Same as {@link org.junit.jupiter.api.BeforeEach}
     */
    @Setup(Level.Trial)
    public void setup() {
    }

    /**
     * You cannot access @Autowired fields from within the @Benchmark annotated method.
     * This is because how the JMH runner runs the benchmarks.
     * Internally, it spawns new Tasks/Threads which leads to losing the @Autowired field values within the benchmarked method.
     * One solution for this is, to store the @Autowired fields into static fields AND
     * disable forking benchmark execution for the JMH runner
     *
     * @see #executeJmhRunner() .fork(0) option
     */
    @Benchmark
    public void getByMobileNew() {
        countryService.getByMobileNew("+375336644859");
    }

    @Benchmark
    public void getByMobileOld() {
        countryService.getByMobileOld("+375336644859");
    }

}
