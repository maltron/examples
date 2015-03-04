//
//  SaveBackground.h
//  TestBackgroundDispatch
//
//  Created by Mauricio Leal on 2/24/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SaveBackground : NSObject
@property (readonly, nonatomic, getter=isRunning, assign) BOOL running;
@property (readonly, nonatomic, getter=isCancelled, assign) BOOL cancelled;

-(void)start;
-(void)stop;

@end
