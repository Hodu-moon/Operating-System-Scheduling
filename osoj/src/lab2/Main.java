package lab2;

import java.util.*;

class Process {
    private String name;        // 프로세스 이름
    private int arrivalTime;    // 프로세스 도착시간
    private int serviceTime;    // 프로세스 서비스시간, 실행해야 할 총 시간
    private int executionTime;    // 프로세스의 현재까지 실행된 시간

    // 문제 2-3: 프로세스의 각 필드를 초기화함
    Process(String name, int arrivalTime, int serviceTime) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        executionTime = 0;
    }

    // 문제 2-3: 프로세스의 현재까지 실행된 시간을 증가시킴
    public void    incExecTime()          { executionTime++; }

    // 문제 2-3: 프로세스의 서비스시간을 반환함
    public int  getServiceTime()          { return serviceTime; }

    // 문제 2-3: cTime은 현재시간임. 시스템에 도착한 이후 ready queue에서 현재시간까지 대기한 대기시간을 반환함
    // cTime과 arrivalTime을 이용한 계산
    public int  getWaitingTime(int cTime) { return cTime - arrivalTime ; }

    // 문제 2-3: 앞으로 더 실행해야 하는 남은 실행시간을 반환함
    public int  getRemainingTime()        { return serviceTime - executionTime; }

    // 문제 2-3: 프로세스의 실행이 종료되었는지 체크함: serviceTime과 executionTime의 상관관계
    public boolean isFinished()           {
        if (serviceTime <= executionTime)
            return true;
        else
            return false;
    }

    // 문제 2-3: cTime은 현재시간임. 프로세스의 응답비율(Response Ratio)를 계산해 반환함
    // 계산시 double로 변환 후 계산해야 함; 위 getWaitingTime(int cTime)을 활용할 것
    public double  getResponeRatioTime(int cTime)  {
        return ((double) getWaitingTime(cTime)+ serviceTime) /serviceTime;
    }

    // 프로세스의 이름을 반환함
    public String  getName()              { return name; }

    public void println(int cTime) {
        System.out.printf("%s: s(%d) e(%d) r(%d) w(%2d) rr(%5.2f) f(%s)\n",
                name, getServiceTime(), executionTime, getRemainingTime(),
                getWaitingTime(cTime), getResponeRatioTime(cTime), isFinished());
    }

    public String toString() {
        return String.format("%s: a(%2d) s(%d) e(%d)",
                name, arrivalTime, serviceTime, executionTime);
    }
}
class Jobs
{
    // 도착할 각 프로세스의 이름, 도착시간, 서비스시간 등을 배열로 관리함
    private String processNames[];
    private int    arrivalTimes[];
    private int    serviceTimes[];
    private int    index; // 다음 번에 도착할 프로세스의 위 배열 인덱스

    public void printJobs() {
        for (String n: processNames)
            System.out.printf("%2s ", n);
        System.out.println();
        for (int t: arrivalTimes)
            System.out.printf("%2d ", t);
        System.out.println();
        for (int s: serviceTimes)
            System.out.printf("%2d ", s);
        System.out.println();
    }

    public Jobs(Scanner s) {  // 생성자
        // 실행할 총 프로세스의 개수를 입력 받음
        System.out.print("The number of processes? ");
        int num = s.nextInt();

        // num개의 원소를 가지는 문자열 processNames[] 배열을 생성
        // 적절한 입력용 메시지를 출력하고("input ? process names : ")
        // for 문을 이용하여 num개  프로세스들의 이름을 입력 받아 processNames[] 배열에 저장
        System.out.print("input "+num+" process names : ");
        processNames = new String[num];
        for(int i = 0; i < num; i++){
            processNames[i] = s.next();
        }

        // num개의 원소를 가지는 정수형 arrivalTimes[] 배열을 생성
        // 적절한 입력용 메시지를 출력하고
        // for 문을 이용하여 num개  프로세스들의 도착시간을 입력 받아 arrivalTimes[] 배열에 저장
        System.out.print("input "+num+" arrival times: ");
        arrivalTimes = new int[num];
        for(int i = 0; i < num; i++){
            arrivalTimes[i] = s.nextInt();
        }

        // num개의 원소를 가지는 정수형 serviceTimes[] 배열을 생성
        // 적절한 입력용 메시지를 출력하고
        // for 문을 이용하여 num개  프로세스들의 서비스시간을 입력 받아 serviceTimes[] 배열에 저장
        System.out.print("input "+num+" service times: ");
        serviceTimes = new int[num];
        for(int i = 0; i < num; i++){
            serviceTimes[i] = s.nextInt();
        }

        System.out.println();
        printJobs();
    }

    // 처음부터 다시 스케줄링을 시작하고자 하는 경우 호출
    public void    reset()          { index = 0; }

    // 아직 도착하지 않은 프로세스가 더 있는지 조사
    public boolean hasNextProcess() { return index < arrivalTimes.length; }

    public void processTest() {
        reset();
        LinkedList<Process> rq = new LinkedList<>();

        System.out.println("Create processes and print their member data.");
        for (int i = 0; i < processNames.length; ++i) {
            Process p = new Process(processNames[i], arrivalTimes[i], serviceTimes[i]);
            rq.add(p);
            System.out.println(p); // 각 프로세스의 멤버 변수들을 출력한다.
        }
        for (Process p: rq) {
            int eTime = p.getServiceTime(); // 이 값이 실행시간이 되도록 할 것이다.
            if (eTime > 3) // 서비스시간이 3보다 큰 경우 실행시간을 반으로 설정하기 위함임
                eTime = (int)(eTime * 0.5 + 0.5); // 실행시간의 반을 반올림
            for (int i = 0; i < eTime; ++i) // 실행시간을 1씩 증가시킨다.
                p.incExecTime();
        }
        System.out.println("\nPrint returned values of member methods of each process.");

        for (Process p: rq) // 각 프로세스의 멤버 메소드의 반환값들을 출력한다.
            p.println(40);  // 40은 현재시간을 의미함
    }


}

public class Main
{
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        Jobs jobs = null;

        while (true) {
            System.out.println("************************ Main Menu *******************");
            System.out.println("* 0.Exit  1.Jobs 2.Process                           *");
            System.out.println("* 3.FCFS  4.SPN  5.HRRN  6.SRT  7.RR(q=1)  8.RR(q=4) *");
            System.out.println("******************************************************");
            System.out.print("Menu item number? ");

            int idx = scan.nextInt();
            if (idx == 0)
                break;

            switch (idx) {
                case 1: jobs = new Jobs(scan);
                    break;
                case 2:
                    if (jobs == null)
                        System.out.println("Jobs is not initalized. "+
                                "Run menu item [1.Jobs] in advance.");
                    else
                        jobs.processTest();
                    break;
                default: System.out.println("WRONG menu item\n");
                    break;
            }
            System.out.println();
        }
        System.out.println("Good bye.");
        scan.close();
    }
}

