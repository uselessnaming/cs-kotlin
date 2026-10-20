package thread_coroutine


import java.util.concurrent.locks.ReentrantLock

class MyThread(private val runnable: Runnable) {
    private var state = State.NEW
    private val reentrantLock = ReentrantLock()
    private val condition = reentrantLock.newCondition()
    private val internalThread: Thread = Thread {
        run()
        updateState(State.TERMINATED)
    }

    fun start() {
        if (state != State.NEW) {
            throw RuntimeException("thread is already running")
        }

        updateState(State.RUNNABLE)
        internalThread.start()
    }

    fun run() {
        runnable.run()
    }

    fun join() {
        reentrantLock.lock()
        try {
            while(state != State.TERMINATED) {
                condition.await()
            }
        } finally {
            reentrantLock.unlock()
        }
    }

    private fun updateState(newState: State) {
        reentrantLock.lock()
        try {
            println("state change : ${state.name} -> ${newState.name}")
            state = newState
            condition.signalAll()
        } finally {}
    }

    private enum class State {
        NEW, // 객체 생성 후 start() 호출 전
        RUNNABLE, // start 호출 이후 JVM에서 실행 or CPU 할당 대기
        BLOCKED, // synchronized 블록/메소드에 진입 도중 다른 thread의 점유로 인해 대기하는 상태
        WAITING, // 다른 스레드가 특정 작업을 완료해 주기를 무기한 대기하는 상태
        TIMED_WAITING, // 시간 제한 대기 상태 (지정된 시간 동안 대기)
        TERMINATED // run() 종료 or 예외로 인한 중단
    }
}

/**
 * Q1
 * Object 클래스는 상속 구조를 보면 모든 class는 Object()를 상속받고 있는데
 * 왜 다른 클래스들에서는 notifyAll(), wait()이 동작하지 않을까?
 * >> 그래서 결국 Lock()이 따로 생겼다네요
 * */