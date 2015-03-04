//
//  SaveBackground.m
//  TestBackgroundDispatch
//
//  Created by Mauricio Leal on 2/24/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "SaveBackground.h"

@interface SaveBackground()

@end

@implementation SaveBackground
@synthesize running = _running, cancelled = _cancelled;

-(void)start {
    if([self isRunning]) return;
    
    NSLog(@">>> [SaveBackground start");
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        _running = YES; _cancelled = NO;
        NSLog(@">>> [SaveBackground run] START");
        for(NSInteger i=0; i < 10; i++) {
            NSLog(@">>> [SaveBackground run] Running %li", (long)i);
            [NSThread sleepForTimeInterval:2.0f];
            
            if(_cancelled) break;
        }
        
        
        NSLog(@">>> [SaveBackground run] STOP");
        _running = NO;
    });
}


-(void)stop {
    _cancelled = YES;
}


@end
