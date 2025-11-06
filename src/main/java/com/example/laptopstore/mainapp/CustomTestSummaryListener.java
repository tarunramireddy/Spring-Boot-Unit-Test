package com.example.laptopstore.mainapp;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.engine.TestExecutionResult;

public class CustomTestSummaryListener implements TestExecutionListener {
    
    private int testsStarted = 0;
    private int testsSucceeded = 0;
    private int testsFailed = 0;
    private int testsSkipped = 0;

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        System.out.println("=== Test Execution Started ===");
    }

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        if (testIdentifier.isTest()) {
            testsStarted++;
        }
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (testIdentifier.isTest()) {
            switch (testExecutionResult.getStatus()) {
                case SUCCESSFUL:
                    testsSucceeded++;
                    System.out.println("✓ PASSED: " + testIdentifier.getDisplayName());
                    break;
                case FAILED:
                    testsFailed++;
                    System.out.println("✗ FAILED: " + testIdentifier.getDisplayName());
                    testExecutionResult.getThrowable().ifPresent(throwable -> 
                        System.out.println("  Error: " + throwable.getMessage())
                    );
                    break;
                case ABORTED:
                    testsSkipped++;
                    System.out.println("⊘ SKIPPED: " + testIdentifier.getDisplayName());
                    break;
            }
        }
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        System.out.println("\n=== Test Execution Summary ===");
        System.out.println("Tests started:   " + testsStarted);
        System.out.println("Tests succeeded: " + testsSucceeded);
        System.out.println("Tests failed:    " + testsFailed);
        System.out.println("Tests skipped:   " + testsSkipped);
        System.out.println("==============================");
    }
}
