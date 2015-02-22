//
//  NetworkService.h
//  TestMOC
//
//  Created by Mauricio Leal on 2/21/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NetworkService : NSObject

-(BOOL)isSuccessfull:(NSHTTPURLResponse *)response;
-(BOOL)isRedirection:(NSHTTPURLResponse *)response;
-(BOOL)isError:(NSHTTPURLResponse *)response;
-(BOOL)isServerError:(NSHTTPURLResponse *)response;

-(NSString *)debug:(NSURL *)url;

-(void)logRequest:(NSString *)method httpMethod:(NSString *)httpMethod url:(NSURL *)url data:(NSData *)data;
-(void)logResponse:(NSString *)method response:(NSHTTPURLResponse *)response data:(NSData *)data;

@end
