# SimpleEncoder
"# SerializerBench" 

What is tested:
A simple message contains 7 fields got encoder/decode 10 Million times in a loop. 

string clOrdId = 1;

string symbol = 2;
  
uint64 px = 3;

uint64 qty = 4;

string dest = 5;

Side side = 6;

OrdType ordType = 7;

##Result:
JavaSerialize Total(sec):105.517442101 Avg(microsecond):10.5517442101

ProtoBuf Total(sec):3.180533399 Avg(microsecond):0.31805333990000006

SBE Normal Total(sec):1.4282401 Avg(microsecond):0.14282401

SBE NoGC Total(sec):0.4797329 Avg(microsecond):0.04797329