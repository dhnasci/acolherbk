package br.manaus.mysoft.acolherbk.utils;

public class TestUtils {

    /**
     * Verifica se o código está sendo executado dentro de um teste unitário.
     * Funciona para JUnit 4 e frameworks que possuem "org.junit" na stack trace.
     *
     * @return true se estiver rodando em um teste unitário, false caso contrário
     */
    public static boolean isRunningUnderTest() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        for (StackTraceElement element : stackTrace) {
            String className = element.getClassName();
            if (className.startsWith("org.junit") ||
                    className.startsWith("org.mockito") ||
                    className.contains("Test")) { // opcional: detectar classes de teste do projeto
                return true;
            }
        }
        return false;
    }
}
