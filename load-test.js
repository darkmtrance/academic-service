import http from "k6/http";
import { check, sleep } from "k6";
import { Rate } from "k6/metrics";

const failureRate = new Rate("check_failure_rate");

export const options = {
    stages: [
        { duration: "10s", target: 10 },  // Ramp up a 10 usuarios en 10 segundos
        { duration: "10s", target: 20 },  // Ramp up a 20 usuarios en 10 segundos
        { duration: "10s", target: 0 },   // Ramp down a 0 en 10 segundos
    ],
    thresholds: {
        http_req_duration: ["p(95)<500"], // 95% de las peticiones deben completarse en menos de 500ms
        check_failure_rate: ["rate<0.1"],  // Menos del 10% de los checks pueden fallar
    },
};

export default function () {
    const BASE_URL = "http://localhost:8080/api";
    
    const params = {
        headers: {
            'Accept-Encoding': 'gzip'
        }
    };
    
    // GET request para obtener todos los estudiantes
    const response = http.get(`${BASE_URL}/students`, params);
    
    // Verificar la respuesta
    const checkRes = check(response, {
        "status is 200": (r) => r.status === 200,
        "response time < 500ms": (r) => r.timings.duration < 500,
        "response has students": (r) => r.json().length > 0,
    });

    failureRate.add(!checkRes);

    // Esperar entre 1 y 5 segundos entre peticiones
    sleep(Math.random() * 4 + 1);
}
